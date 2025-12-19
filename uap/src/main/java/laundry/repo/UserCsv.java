package laundry.repo;

import laundry.exception.Data;
import laundry.model.User;
import laundry.util.CsvUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserCsv {
    private final File file;

    public UserCsv(String path) {
        this.file = new File(path);
        ensureParentDir();
    }

    private void ensureParentDir() {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        try { if (!file.exists()) file.createNewFile(); } catch (IOException ignored) {}
    }

    public List<User> loadAll() throws Data {
        List<User> users = new ArrayList<>();
        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine && line.startsWith("username")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;
                String[] parts = CsvUtil.parseLine(line);
                if (parts.length >= 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (Exception e) {
            throw new Data("Gagal membaca file CSV", e);
        }
        return users;
    }

    public void saveAll(List<User> users) throws Data {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("username,password");
            for (User u : users) {
                pw.println(String.join(",",
                        CsvUtil.escape(u.getUsername()),
                        CsvUtil.escape(u.getPassword())
                ));
            }
        } catch (Exception e) {
            throw new Data("Gagal menulis file CSV", e);
        }
    }

    public void addUser(User user) throws Data {
        List<User> users = loadAll();
        users.add(user);
        saveAll(users);
    }

    public User findByUsername(String username) throws Data {
        List<User> users = loadAll();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}

