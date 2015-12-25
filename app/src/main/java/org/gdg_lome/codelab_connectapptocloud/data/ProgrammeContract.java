
package org.gdg_lome.codelab_connectapptocloud.data;

import android.provider.BaseColumns;

/**
 * Created by setico on 10/10/15.
 */
public class ProgrammeContract {

    public static final class MatiereEntry implements BaseColumns{

        public static String TABLE_NAME = "matiere";
        public static String COLUMN_MATIERE_LOGO = "logo";
        public static String COLUMN_MATIERE_NOM = "nom";
        public static String COLUMN_MATIERE_DESCRIPTION = "description";
    }
}
