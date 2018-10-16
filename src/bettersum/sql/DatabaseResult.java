package bettersum.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DatabaseResult implements AutoCloseable {

    private ResultSet resultSet;

    public DatabaseResult(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public <T> List<T> getEntries(int amount, int column, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        try {
            int count = 0;
            while (count++ < amount && this.resultSet.next()) {
                results.add(this.resultSet.getObject(column, clazz));
            }
        } catch (SQLException e) {
            System.err.println("Could not get results");
            e.printStackTrace();
        }
        return results;
    }

    public <T> List<T> getAllEntries(int column, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        try {
            while (this.resultSet.next()) {
                results.add(this.resultSet.getObject(column, clazz));
            }
        } catch (SQLException e) {
            System.err.println("Could not get results");
            e.printStackTrace();
        }
        return results;
    }

    public List<?> getObjects(int column) {
        return this.getAllEntries(column, Object.class);
    }

    public List<Integer> getInts(int column) {
        return this.getAllEntries(column, int.class);
    }

    public List<Long> getLongs(int column) {
        return this.getAllEntries(column, long.class);
    }

    public List<String> getStrings(int column) {
        return this.getAllEntries(column, String.class);
    }

    public List<Boolean> getBooleans(int column) {
        return this.getAllEntries(column, boolean.class);
    }

    public <K, V> Map<K, V> getKeyValueMap(int keyColumn, Class<K> keyClazz, int valueColumn, Class<V> valueClazz) {
        Map<K, V> map = new LinkedHashMap<>();
        try {
            while (this.resultSet.next()) {
                map.put(this.resultSet.getObject(keyColumn, keyClazz), this.resultSet.getObject(valueColumn, valueClazz));
            }
        } catch (SQLException e) {
            System.err.println("Could not get key-value results");
            e.printStackTrace();
        }
        return map;
    }

    public <T> T get(int column, Class<T> clazz) {
        try {
            return this.resultSet.getObject(column, clazz);
        } catch (SQLException e) {
            System.err.println("Could not get result");
            e.printStackTrace();
        }

        return null;
    }

    public Object getObject(int column) {
        try {
            return this.resultSet.getObject(column);
        } catch (SQLException e) {
            System.err.println("Could not get result");
            e.printStackTrace();
        }

        return null;
    }

    public int getInt(int column) {
        return this.get(column, int.class);
    }

    public long getLong(int column) {
        return this.get(column, long.class);
    }

    public String getString(int column) {
        return this.get(column, String.class);
    }

    public boolean getBoolean(int column) {
        return this.get(column, boolean.class);
    }

    public boolean move() {
        try {
            return this.resultSet.next();
        } catch (SQLException e) {
            System.err.println("Could not move result set");
            e.printStackTrace();
        }
        return false;
    }

    public boolean toBeginning() {
        try {
            return this.resultSet.first();
        } catch (SQLException e) {
            System.err.println("Could not move result set");
            e.printStackTrace();
        }
        return false;
    }

    public boolean move(int row) {
        try {
            this.resultSet.beforeFirst();
            while (this.resultSet.next() && this.resultSet.getRow() < row);
            return this.resultSet.getRow() == row;
        } catch (SQLException e) {
            System.err.println("Could not move cursor to specified position");
            e.printStackTrace();
        }
        return false;
    }

    public int indexOf(String columnName) {
        try {
            return this.resultSet.findColumn(columnName);
        } catch (SQLException e) {
            System.err.println("Could not find column");
            e.printStackTrace();
        }
        return -1;
    }

    public int columns() {
        try {
            return this.resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            System.err.println("Could not get columns of this result");
            e.printStackTrace();
        }
        return 0;
    }

    public int rows() {
        int count = 0;
        int previousPosition = 0;

        try {
            previousPosition = resultSet.getRow();
        } catch (SQLException e) {
            System.err.println("Could not retrieve current position");
            e.printStackTrace();
        }

        try {

            this.resultSet.first();
            while (this.resultSet.next()) {
                count++;
            }

        } catch (SQLException e) {
            System.err.println("Could not count rows");
            e.printStackTrace();
        } finally {
            this.move(previousPosition);
        }
        return count;
    }

    @Override
    public void close() throws SQLException {
        this.resultSet.getStatement().close();
    }
}
