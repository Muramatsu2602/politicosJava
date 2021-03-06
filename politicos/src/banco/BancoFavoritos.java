package banco;


import java.sql.*;
import javax.swing.*;
import java.util.*; //arraylist

public class BancoFavoritos {

    private int bdid;
    private String bdtipo;
    private String bdNome;
    private String fsql;
    private String url, usuario, senha_banco, drive;
    private Connection con;
    private ResultSet rs;
    private PreparedStatement pstmt;
    private Statement stmt;

    // CAMPOS DA TABELA
   
    private Favorito fav;
    
    public BancoFavoritos() {
        bdid = 0;
        bdtipo = "";
        fsql = "";
        con = null;
        usuario = "postgres";
        senha_banco = "postgres";
        drive = "org.postgresql.Driver";
        url = "jdbc:postgresql://localhost:5432/PoliticosJava";
        fav = new Favorito();
        //url="jdbc:postgresql://200.145.153.163:5432/banco73b2017";  
    }
      public BancoFavoritos(Favorito fav) {
        bdid = 0;
        bdtipo = "";
        fsql = "";
        con = null;
        usuario = "postgres";
        senha_banco = "postgres";
        drive = "org.postgresql.Driver";
        url = "jdbc:postgresql://localhost:5432/PoliticosJava";
        this.fav = fav;
        //url="jdbc:postgresql://200.145.153.163:5432/banco73b2017";  
    }

    // METODOS BANCO - USuario
    public void incluir()//pkchave (chamada,turma)
    {
        fsql = "INSERT INTO favoritos ("
                + "id_favorito,"
                + "deputado_id,"
                + "nome,"
                + "usuario_id,"
                + "partido,"
                + "estado,"
                + "status"
                + ")"
                + "VALUES (?,?,?,?,?,?,?)";
        try {
            pstmt = con.prepareStatement(fsql);
            pstmt.setString(1, this.fav.getIdFavorito());
            pstmt.setString(2, this.fav.getDeputadoId());
            pstmt.setString(3, this.fav.getNome());
            pstmt.setString(4, this.fav.getUsuarioId());
            pstmt.setString(5, this.fav.getPartido());
            pstmt.setString(6, this.fav.getEstado());
            pstmt.setInt(7, 1);

            pstmt.execute();
            pstmt.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro na inclusao tabela Favoritos:" + erro);
        }
    }

    public void excluir() {
        fsql = "DELETE FROM favoritos WHERE deputado_id=? AND usuario_id=?";
        try {
            pstmt = con.prepareStatement(fsql);
            pstmt.setString(1, this.fav.getDeputadoId());
             pstmt.setString(2, this.fav.getUsuarioId());
            pstmt.execute();
            pstmt.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro na exclusao:" + erro);
        }
    }

    // enviar id_usuario via parametro das funcoes?
    public void alterar() {
        fsql = "UPDATE favoritos SET"
                + " status=1"
                + "WHERE id_deputado=? ;";
        try {
            pstmt = con.prepareStatement(fsql);

            //pstmt.setString(1,bdnome);
            pstmt.setString(1, this.fav.getDeputadoId());
           
            pstmt.execute();
            pstmt.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro na alteracao tabela Favoritos:" + erro);
        }
    }

    public void connect() {
        try {
            Class.forName(drive);
            con = DriverManager.getConnection(url, usuario, senha_banco);
            //JOptionPane.showMessageDialog(null,"ok");
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro na conexao:" + erro);
        }
    }//conectyyyyyy

    public void disconnect() {
        try {
            con.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro na desconexao:" + erro);
        }
    } ///////////desconecta

    public ArrayList<Favorito> pegadados() {
        ArrayList dados;
        dados = new ArrayList();
        fsql = "SELECT * FROM favoritos WHERE status=1;";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(fsql);
            while (rs.next()) {
                /*
                 bdid = rs.getInt("id_favoritos");
                 bdtipo = rs.getString("tipo"); // ?? --> campo da tabela
                 dados.add(bdid);
                 dados.add(bdtipo);*/
                this.fav.setIdFavorito(rs.getString("id_favoritos"));
                this.fav.setDeputadoId(rs.getString("deputado_id"));
                this.fav.setNome( rs.getString("nome"));
                this.fav.setEstado(rs.getString("estado"));
                this.fav.setPartido(rs.getString("partido"));
                this.fav.setUsuarioId(rs.getString("usuario_id"));
                this.fav.setStatus(rs.getInt("status"));
                dados.add(this.fav);
                
                this.fav = null;
                this.fav = new Favorito();

                /*
                 private String nome;
                 private int usuario_id;
                 private int deputado_id;
                 private String partido;
                 private String foto; // 
                 */
            }///while	
            stmt.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro na leitura:" + erro);
        }
        return dados;
    }//pegadados

    public List<Favorito> procura(String id) {
        fsql = "SELECT * FROM favoritos WHERE usuario_id=? AND status=1;";
        List favs = new ArrayList();
        Favorito fav = new Favorito();
        try {
            
            pstmt = con.prepareStatement(fsql);
            pstmt.setString(1,id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
               fav.setPartido(rs.getString("partido"));
                fav.setNome(rs.getString("nome"));
                fav.setStatus(rs.getInt("status"));
                fav.setDeputadoId(rs.getString("deputado_id"));
                fav.setEstado(rs.getString("estado"));
                fav.setUsuarioId(rs.getString("usuario_id"));
                
                favs.add(fav);
                fav = null;
                fav = new Favorito();
            }
            pstmt.close();
            return favs;
        } catch (Exception erroi) {
            JOptionPane.showMessageDialog(null,
                    " Erro procura do id_favoritos (" + id + ")-->" + erroi);
        }
        return null;
    }//procura 
public Favorito procura(String idu, String idd) {
        fsql = "SELECT * FROM favoritos WHERE usuario_id=? AND deputado_id=? AND status=1;";
        Favorito fav = new Favorito();
        try {
            
            pstmt = con.prepareStatement(fsql);
            pstmt.setString(1,idu);
            pstmt.setString(2,idd);
            rs = pstmt.executeQuery();
            if (rs.next()) {
               fav.setPartido(rs.getString("partido"));
                fav.setNome(rs.getString("nome"));
                fav.setStatus(rs.getInt("status"));
                fav.setDeputadoId(rs.getString("deputado_id"));
                fav.setEstado(rs.getString("estado"));
                fav.setUsuarioId(rs.getString("usuario_id"));
            }
            pstmt.close();
            return fav;
        } catch (Exception erroi) {
            JOptionPane.showMessageDialog(null,
                    " Erro procura do id_favoritos (" + idd + ")-->" + erroi);
        }
        return null;
    }//procura 

    /*
     // CONSULTAR FOTO DO SITE (tipo seria um campo especifico de la, se refere a tipo de aluno no caso)
    
     public String pegatipo(String id)
     {
     fsql="Select * from tabela2 where id=?";
     try{pstmt = con.prepareStatement(fsql);
     int idd=Integer.parseInt(id);
     pstmt.setInt(1, idd);
     rs=pstmt.executeQuery();
     if(rs.next())
     { return rs.getString("tipo");
     }    
     pstmt.close();    
     }    catch(Exception erroi)
     {
     JOptionPane.showMessageDialog(null,
     " Erro procura:"+erroi);
     }
     return "vazio";
     }//procura 
     */
    public String retorna() {
        String volta = "";
        fsql = "SELECT * FROM favoritos";
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            // executa
            rs = stmt.executeQuery(fsql);
            if (rs.last()) {
                volta = rs.getString("id_favoritos");
                //JOptionPane.showMessageDialog(null,"ultimo"+volta);

                return volta;
            }
        } catch (Exception erroi) {
            JOptionPane.showMessageDialog(null,
                    " Erro leitura :" + erroi);
        }
        return volta;
    }
    
    
    
    // ENCAPSULAMENTO
    public void setId(String c) {
        bdid = Integer.parseInt(c);
    }

    public void setTipo(String n) {
        bdtipo = n;
    }

    public int getId() {
        return bdid;
    }

    public String getTipo() {
        return bdtipo;
    }
    
    

    /*
    
     // TAVA LA NO CODIGO DELE
    
     public static void main(String oi[]) {
     bancotipo b2 = new bancotipo();
     b2.connect();
     b2.setNome("tipocti bauru");
     b2.incluir();
     b2.disconnect();
     }*/
 
}// CLASS
