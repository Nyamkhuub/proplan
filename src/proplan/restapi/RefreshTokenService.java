package proplan.restapi;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class RefreshTokenService extends ScheduledService<Object> {
    @Override
    protected Task<Object> createTask() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                return null;
            }
        };
    }
}
