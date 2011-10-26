/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Usuario
 */
public class MysqlBackUp {

    private final int BUFFER = 10485760;
    private String host;
    private String port;
    private String user;
    private String password;
    private String db;
    private String mysqldump;

    public MysqlBackUp(String database_host, String database_port,
            String database_user, String database_password, String database_name,
            String mysqldumpPath) {
        setSettings(database_host, database_port, database_user,
                database_password, database_name, mysqldumpPath);
    }

    public MysqlBackUp(String database_host, String database_port,
            String database_user, String database_password, String database_name) {
        setSettings(database_host, database_port, database_user,
                database_password, database_name, null);
    }

    public final void setSettings(String database_host, String database_port,
            String database_user, String database_password, String database_name,
            String mysqldumpPath) {
        host = database_host;
        port = database_port;
        user = database_user;
        password = database_password;
        db = database_name;
        mysqldump = mysqldumpPath;
    }

    public String getData() throws Exception {
        String mysqldump_ = mysqldump == null ? "mysqldump" : mysqldump;
        Process run = Runtime.getRuntime().exec(mysqldump_ + " --host="
                + host + " --port=" + port + " --user=" + user + " --password="
                + password + " --compact --complete-insert --extended-insert "
                + "--skip-comments --skip-triggers " + db);
        return getProcessText(run);
    }

    public String getRoutine() throws Exception {
        String mysqldump_ = mysqldump == null ? "mysqldump" : mysqldump;
        Process run = Runtime.getRuntime().exec(mysqldump_ + " --host=" + host
                + " --port=" + port + " --user=" + user + " --password="
                + password + " --compact --skip-comments --no-create-info "
                + "--no-data --routines " + db);
        return getProcessText(run);
    }

    private String getProcessText(Process run) throws IOException {
        InputStream in = run.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder temp = new StringBuilder();
        int count;
        char[] cbuf = new char[BUFFER];
        while ((count = br.read(cbuf, 0, BUFFER)) != -1) {
            temp.append(cbuf, 0, count);
        }
        br.close();
        in.close();
        return temp.toString();
    }
}
