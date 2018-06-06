package tr.com.metu.ramazan.commands;

import java.io.IOException;

import org.jline.reader.impl.history.DefaultHistory;
import org.springframework.stereotype.Component;

/**
 * This class is for disabling command log
 *
 * @author yasar
 */
@Component
public class NoSaveHistory extends DefaultHistory {
    @Override
    public void save() throws IOException {

    }
}
