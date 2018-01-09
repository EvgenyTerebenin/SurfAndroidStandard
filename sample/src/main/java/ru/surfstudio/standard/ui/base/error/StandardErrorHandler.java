package ru.surfstudio.standard.ui.base.error;


import android.text.TextUtils;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.standard.R;
import ru.surfstudio.standard.interactor.common.network.error.ConversionException;
import ru.surfstudio.standard.interactor.common.network.error.HttpCodes;
import ru.surfstudio.standard.interactor.common.network.error.HttpProtocolException;
import ru.surfstudio.standard.interactor.common.network.error.NoContentException;
import ru.surfstudio.standard.interactor.common.network.error.NoInternetException;

/**
 * Стандартный обработчик ошибок, возникающих при работе с сервером
 */
@PerScreen
public class StandardErrorHandler extends NetworkErrorHandler {

    private final MessageController messageController;

    @Inject
    public StandardErrorHandler(MessageController messageController) {
        this.messageController = messageController;
    }

    @Override
    protected void handleHttpProtocolException(HttpProtocolException e) {
        if (e.getHttpCode() >= HttpCodes.CODE_500) {
            messageController.show(R.string.server_error_message);
        } else if (e.getHttpCode() == HttpCodes.CODE_403) {
            messageController.show(R.string.forbidden_error_error_message);
        } else if (!TextUtils.isEmpty(e.getServerMessage())) {
            messageController.show(e.getServerMessage());
        } else if (e.getHttpCode() == HttpCodes.CODE_404) {
            messageController.show(R.string.server_error_not_found);
        } else {
            messageController.show(R.string.default_http_error_message);
        }
    }

    @Override
    protected void handleNoInternetError(NoInternetException e) {
        messageController.show(R.string.no_internet_connection_error_message);
    }

    @Override
    protected void handleNoContentError(NoContentException e) {
        messageController.show(R.string.no_content_error_message);
    }

    @Override
    protected void handleConversionError(ConversionException e) {
        messageController.show(R.string.bad_response_error_message);
    }

    @Override
    protected void handleOtherError(Throwable e) {
        messageController.show(R.string.unexpected_error_error_message);
        Logger.e(e, "Unexpected error");
    }
}
