package org.example;

import java.sql.*;
import java.util.Scanner;

public class MySQLConnection {

    public static void main(String[] args) {
        String query = "";
        Scanner sc= new Scanner(System.in);
        System.out.println("WELCOME TO THE FILMS CLUB");
        System.out.println("*************************");
        System.out.println("QUE DESEA HACER:");
        System.out.println("1.Peliculas estrenadas entre:X y Z");
        System.out.println("2.Peliculas de cada director");
        System.out.println("3.insertar una nueva pelicula");

        int num= sc.nextInt();
        String año1;String año2;
        String s;
        boolean update=false;
        String nombre;String date;String pais;int director;


        switch(num){
            case 1:
                System.out.println("A continuacion diga entre que años quiere buscar ");
                System.out.println("Entre...(ejemplo 1999)");
                año1=sc.next();
                System.out.println("Y...(ejemplo 2011)");
                año2= sc.next();
                System.out.println("Las Peliculas estrenadas entre "+ año1+" y "+ año2+" son...");
                query="SELECT Titol FROM films WHERE YEAR(dataEstrena) BETWEEN "+ "'"+año1+"'"+ " AND "+ "'"+año2+"'";
                break;
            case 2:

                System.out.println("Actualmente hay 3 directores en disponibles:");
                System.out.println("Steven Spielberg, George Lucas y Gareth Edwards");
                System.out.println("A continuacion escriba el nombre del director:");
                s=sc.next();
                System.out.println("El nombre del director es: "+s);
                System.out.println("Buscando...");
                System.out.println("");
                query="SELECT * FROM FILMS f INNER JOIN DIRECTOR d ON f.idDirector = d.idDirector WHERE d.Nom like \"%" + s +  "%\"" ;

                break;
            case 3:
                update=true;

                System.out.println("A continuacion se le pediran algunos datos de la pelicula.");
                System.out.println("Recuerde que solo se pueden introducir peliculas de Steven Spielberg, George Lucas y Gareth Edwards");

                System.out.println("Nombre de la pelicula(Formato cammelCase ejemplo: TomAndJerry)");
                nombre=sc.next();
                System.out.println("Fecha de estreno en formato AAAA-MM-DD");
                date=sc.next();
                System.out.println("¿De donde es la pelicula?(país)");
                pais=sc.next();
                System.out.println("Director: 1 para Steven Spielberg, 2 para George Lucas y  3 para Gareth Edwards");
                System.out.println("Solo escriba los numeros 1,2 o 3 porfavor");
                System.out.println("");
                director=sc.nextInt();
//INSERT INTO films (Titol,DataEstrena,Pais,idDirector) VALUES ('TomAndJerry','1998-06-04','EEUU',1);
                query="INSERT INTO films (Titol,DataEstrena,Pais,idDirector) VALUES ("+"'"+nombre+"'"+","+"'"+date+"'"+","+"'"+pais+"'"+","+director+")";
                break;
            default:
                System.out.println("Se ha detectado un error, de manera automatica el programa enseñara por pantalla todas las peliculas.");
                query="SELECT * FROM Film_peli";
                break;
        }











        // TODO Auto-generated method stub

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/cinema";
        String user = "root";
        String password = "hola";

        try {
            Class.forName(driver);

            try(Connection con = DriverManager.getConnection(url, user, password);
                Statement st = con.createStatement();) {
                ResultSet rs;
                if(!update){
                    rs=st.executeQuery(query);
                    int colNum = getColumnNames(rs);
                    if(colNum>0) {
                        while(rs.next()) {
                            for(int i =0; i<colNum; i++) {
                                if(i+1 == colNum) {
                                    System.out.println(rs.getString(i+1));
                                } else {
                                    System.out.print(rs.getString(i+1)+ ", ");
                                }
                            }//endfor
                        }//endwhile
                    }

                }else {

                        st.executeUpdate(query);
                        System.out.println("nueva Pelicula insertada en Films");

                }


            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }//end try catch

    }

    public static int getColumnNames(ResultSet rs) throws SQLException {
        int numberOfColumns = 0;
        if (rs != null) {
            //create an object based on the Metadata of the result set
            ResultSetMetaData rsMetaData = rs.getMetaData();
            //Use the getColumn method to get the number of columns returned
            numberOfColumns = rsMetaData.getColumnCount();
            //get and print the column names, column indexes start from 1
            for (int i = 1; i < numberOfColumns + 1; i++) {
                String columnName = rsMetaData.getColumnName(i);
                System.out.print(columnName + ", ");
            }//endfor
        }//endif
        //place the cursor on a new line in the console
        System.out.println();
        return numberOfColumns;
    }//end method getColumnNames

}