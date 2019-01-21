/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.core.ui.navigation.activity.route.cross_feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.logger.Logger

/**
 * Cross-feature navigation activity route between two activities in different independent
 * Gradle-projects.
 *
 * @see [ActivityRoute]
 * @see [CrossFeatureRoute]
 */
abstract class ActivityCrossFeatureRoute :
        ActivityRoute(),
        CrossFeatureRoute {

    override fun prepareIntent(context: Context): Intent? {
        try {
            return Intent(context, Class.forName(targetClassPath()))
        } catch (e: ClassNotFoundException) {
            Logger.e("Activity with the following classpath was not found in the current " +
                    "application: ${targetClassPath()}. If this activity is the part of Dynamic Feature, " +
                    "please check if this Dynamic Feature is downloaded and installed on the device" +
                    "successfully.")
        }
        return null
    }

    override fun prepareBundle(): Bundle? {
        return null
    }
}