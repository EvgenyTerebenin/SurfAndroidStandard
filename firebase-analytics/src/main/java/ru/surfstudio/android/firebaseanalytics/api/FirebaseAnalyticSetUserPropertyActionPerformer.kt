package ru.surfstudio.android.firebaseanalytics.api

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer

/**
 * Выполнитель действия setUserProperty на аналитике FirebaseAnalytics
 */
class FirebaseAnalyticSetUserPropertyActionPerformer(private val firebaseAnalytics: FirebaseAnalytics)
    : AnalyticActionPerformer<FirebaseAnalyticSetUserPropertyAction> {

    override fun canHandle(action: AnalyticAction) = action is FirebaseAnalyticSetUserPropertyAction

    override fun perform(action: FirebaseAnalyticSetUserPropertyAction) {
        firebaseAnalytics.setUserProperty(action.key.take(MAX_SET_USER_PROPERTY_KEY_LENGTH), action.value.take(MAX_SET_USER_PROPERTY_VALUE_LENGTH))
    }
}