
class PassLoader {
    public static int loadAll(Connection con, List<String[]> passes) throws SQLException {
        String sql = "INSERT INTO bus_pass(pass_id, reg_no, name, route_no, amount) VALUES(?,?,?,?,?)";
        boolean old = con.getAutoCommit(); con.setAutoCommit(false);
        try (PreparedStatement p = con.prepareStatement(sql)) {
            int n = 0;
            for (String[] row : passes) {
                if (row.length != 5) throw new IllegalArgumentException("Five fields required");
                p.setString(1, row[0]); p.setString(2, row[1]); p.setString(3, row[2]);
                p.setInt(4, Integer.parseInt(row[3])); p.setDouble(5, Double.parseDouble(row[4]));
                p.addBatch(); n++; if (n % 1000 == 0) p.executeBatch();
            }
            if (n % 1000 != 0) p.executeBatch(); con.commit(); return n;
        } catch (SQLException | RuntimeException e) { con.rollback(); throw e; }
        finally { con.setAutoCommit(old); }
    }
}

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
public class PassMigration {
    public static void main(String[] args)throws Exception {
        Path file=Path.of(args.length>0?args[0]:"bus-passes.csv");
        List<String[]> valid=new ArrayList<>();List<String> rejected=new ArrayList<>();
        try(BufferedReader r=Files.newBufferedReader(file)) {
            String line;int number=0;
            while((line=r.readLine())!=null) {
                number++;String[] p=line.split(",",-1);
                try {
                    if(p.length!=5||p[0].isBlank()||p[1].isBlank()||p[2].isBlank())throw new IllegalArgumentException("Invalid fields");
                    int route=Integer.parseInt(p[3]);double amount=Double.parseDouble(p[4]);
                    if(route<0||amount<0)throw new IllegalArgumentException("Negative route or amount");
                    valid.add(p);
                } catch(RuntimeException e) { rejected.add("Line "+number+": "+e.getMessage()); }
            }
        }
        try(Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/java_assignment5","root","password")) {
            System.out.println("Loaded: "+PassLoader.loadAll(c,valid));
            DatabaseMetaData m=c.getMetaData();
            System.out.println("Database: "+m.getDatabaseProductName());
            System.out.println("Driver: "+m.getDriverName()+" "+m.getDriverVersion());
            try(ResultSet t=m.getTables(null,null,"%",new String[]{"TABLE"})) {
                while(t.next())System.out.println("Table: "+t.getString("TABLE_NAME"));
            }
        }
        System.out.println("Rejected: "+rejected.size());for(String e:rejected)System.out.println(e);
    }
}
