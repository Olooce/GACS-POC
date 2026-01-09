package ke.ac.ku.gacs.model;

import java.io.Serializable;


/**
 * GACS (ke.ac.ku.gacs.model)
 * Created by: Oloo
 * On: 07/01/2026 12:16
 * Description: Base interface for problem definitions
 **/

public interface Problem extends Serializable {
    /**
     * Validate problem constraints
     */
    boolean isValid();

    /**
     * Get problem description
     */
    String getDescription();
}

