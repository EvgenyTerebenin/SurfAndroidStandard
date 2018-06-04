/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin.

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
package ru.surfstudio.android.filestorage;


import ru.surfstudio.android.filestorage.naming.NamingProcessor;
import ru.surfstudio.android.filestorage.processor.CacheFileProcessor;

/**
 * базовый класс текстового кеша
 */
public abstract class BaseTextLocalCache extends BaseLocalCache<String> {

    public BaseTextLocalCache(CacheFileProcessor fileProcessor, NamingProcessor namingProcessor) {
        super(fileProcessor, namingProcessor);
    }

    @Override
    public ObjectConverter<String> getConverter() {
        return new ObjectConverter<String>() {
            @Override
            public byte[] encode(String value) {
                return value.getBytes();
            }

            @Override
            public String decode(byte[] rawValue) {
                return new String(rawValue);
            }
        };
    }
}
