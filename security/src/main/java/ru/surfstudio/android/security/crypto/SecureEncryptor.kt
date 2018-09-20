/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.security.crypto

import ru.surfstudio.android.filestorage.encryptor.Encryptor
import javax.crypto.Cipher

/**
 * Класс для шифрования и дешифрования данных с использованием подписи
 */
abstract class SecureEncryptor<T>(protected val sign: T): Encryptor {

    override fun encrypt(decryptedBytes: ByteArray): ByteArray = try {
        val salt = SecurityUtils.generateSalt()
        val cipher = getEncryptCipher(salt)

        SecretValue(cipher.doFinal(decryptedBytes), cipher.iv, salt).toBytes()
    } catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }

    override fun decrypt(rawBytes: ByteArray): ByteArray = try {
        val encrypted = SecretValue.fromBytes(rawBytes)
        val cipher = getDecryptCipher(encrypted.salt, encrypted.iv)

        cipher.doFinal(encrypted.secret)
    } catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }

    abstract fun getEncryptCipher(salt: ByteArray): Cipher

    abstract fun getDecryptCipher(salt: ByteArray, iv: ByteArray): Cipher
}

/**
 * Исключение, которое является оберткой для ошибок, возникающих при шифровании данных
 */
class SecureEncryptorException(throwable: Throwable) : Throwable(throwable)