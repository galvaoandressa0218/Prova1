package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Aula;
import model.AulaDto;

public class Db {

	private static Db instance = null;
	private Connection connection = null;

	private String driver;
	private String url;
	private String user;
	private String password;

	private Db() {
		this.confDB();
		this.conectar();
		this.criarTabela();
	}

	public static Db getInstance() {
		if (instance == null) {
			instance = new Db();
		}
		return instance;
	}

	private void confDB() {
		try {
			this.driver = "org.h2.Driver";
			this.url = "jdbc:h2:mem:testdb";
			this.user = "sa";
			this.password = "";
			Class.forName(this.driver);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
	}

	// Inicia a conexão com o banco de dados
	private void conectar() {
		try {
			this.connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
	}

	private void criarTabela() {
		String query = "CREATE TABLE AULA ("
				+ "    ID BIGINT AUTO_INCREMENT PRIMARY KEY,"
				+ "    COD_DISCIPLINA INT,"
				+ "    ASSUNTO VARCHAR(255),"
				+ "    DURACAO INT,"
				+ "    DATA VARCHAR(20),"
				+ "    HORARIO VARCHAR(20)"
				+ ")";
		try {
			Statement statement = this.connection.createStatement();
			statement.executeUpdate(query);
			this.connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
	}

	// Encerra a conexão
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * ****************************************************************
	 * CRUD
	 * ****************************************************************
	 */

	// CRUD READ
	public ArrayList<AulaDto> findAll() {
		ResultSet result = null;
		PreparedStatement conexao = null;
		String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA;";
		ArrayList<AulaDto> lista = new ArrayList<AulaDto>();
		try {
			conexao = connection.prepareStatement(query);
			result = conexao.executeQuery();


			while(result.next()) {
				Aula aula = new Aula();
				aula.setAssunto(result.getString("ASSUNTO"));
				aula.setCodDisciplina(result.getInt("COD_DISCIPLINA"));
				aula.setData(result.getString("DATA"));
				aula.setDuracao(result.getInt("DURACAO"));
				aula.setHorario(result.getString("HORARIO"));
				aula.setId(result.getLong("ID"));
				AulaDto aulaDto =new  AulaDto(aula);
				lista.add(aulaDto);
			}

		return lista;
		}catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}  finally {
	        try {
	        	result.close();
	        	conexao.close();
	        }catch(SQLException e) {
	        	throw new DatabaseException(e.getMessage());
	        }
	    }
	}

	public AulaDto findById(String id) {
		String query = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA "
				+ "WHERE ID = ?";
		PreparedStatement conexao = null;
		ResultSet result = null;
		
		try {
			conexao = connection.prepareStatement(query);
			conexao.setString(1, id);
			
			result = conexao.executeQuery();
			
			if(result.next()) {
				Aula aula = new Aula();
				aula.setAssunto(result.getString("ASSUNTO"));
				aula.setCodDisciplina(result.getInt("COD_DISCIPLINA"));
				aula.setData(result.getString("DATA"));
				aula.setDuracao(result.getInt("DURACAO"));
				aula.setHorario(result.getString("HORARIO"));
				aula.setId(result.getLong("ID"));
				AulaDto aulaDto = new AulaDto(aula);
				return aulaDto;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		finally {
			 try {
		        	result.close();
		        	conexao.close();
		        }catch(SQLException e) {
		        	throw new DatabaseException(e.getMessage());
		        }
		}
	}

	// CRUD CREATAE
	public void create(AulaDto dto) {
		String query = "INSERT INTO AULA (COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO) "
				+ "VALUES (?,?,?,?,?)";
		/*
		 * 	Crie um PreparedStatement que inclua todos os campos a serem registrados na
		 * 	tabela. Lembre-se de que a contagem dos parâmetros (?) começa em 1.
		 * 	Observe o método delete abaixo. Pode ser útil.
		 */
		PreparedStatement conexao = null;
		try {
			conexao = this.connection.prepareStatement(query);
			Aula aula = new Aula(dto);
			conexao.setInt(1, aula.getCodDisciplina());
			conexao.setString(2, aula.getAssunto());
			conexao.setInt(3, aula.getDuracao());
			conexao.setString(4, aula.getData());
			conexao.setString(5, aula.getHorario());
			conexao.execute();
			conexao = this.connection.prepareStatement(query);
		} catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	// CRUD DELETE
	public void deleteAll() {
		String query = "DELETE FROM AULA";
		Statement st = null;
		try {
			st = this.connection.createStatement();
			st.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// CRUD DELETE
	public void delete(String id) {
		String query = "DELETE FROM AULA WHERE ID = ?";
		PreparedStatement pst = null;
		try {
			pst = this.connection.prepareStatement(query);
			pst.setString(1, id);
			pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// CRUD UPDATE
	public void update(AulaDto dto) {
		String query = "UPDATE AULA SET "
				+ "COD_DISCIPLINA = ?, ASSUNTO = ?, DURACAO = ?, DATA = ?, HORARIO = ? "
				+ "WHERE ID = ?";
		/*
		 * 	Use os atributos do DTO para atualizar o dado associado ao id.
		 * 	Use PreparedStatement, lembrando que a contagem dos parâmetros (?)
		 * 	começa em 1.
		 */
		PreparedStatement conexao = null;
		try {
			conexao = this.connection.prepareStatement(query);
			conexao.setInt(1, Integer.parseInt(dto.codDisciplina)); 
			conexao.setString(2, dto.assunto); 
			conexao.setInt(3, Integer.parseInt(dto.duracao)); 
			conexao.setString(4, dto.data);
			conexao.setString(5, dto.horario);
			conexao.setString(6, dto.id);
			conexao.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new DatabaseException(e.getMessage());
	    } finally {
	    	try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	}

	/*
	 * PARA EFEITO DE TESTES
	 */

	public void reset() {
		this.deleteAll();
		this.popularTabela();
	}

	public void popularTabela() {
		AulaDto dto = new AulaDto();

		dto.codDisciplina = "1";
		dto.assunto = "Derivadas";
		dto.duracao = "2";
		dto.data = "2024-04-12";
		dto.horario = "14:00";
		this.create(dto);

		dto.codDisciplina = "3";
		dto.assunto = "Coordenadas Cartesianas";
		dto.duracao = "2";
		dto.data = "2024-04-13";
		dto.horario = "14:00";
		this.create(dto);

		dto.codDisciplina = "4";
		dto.assunto = "O Problema dos Três Corpos";
		dto.duracao = "4";
		dto.data = "2024-04-14";
		dto.horario = "14:00";
		this.create(dto);
	}

}
