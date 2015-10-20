package com.sgop.principal;

import android.content.Context;
import android.util.Log;

import com.sgop.dao.CadUsuarioDAO;
import com.sgop.dao.Cad_BoletoDAO;
import com.sgop.dao.Cad_EntradaDAO;
import com.sgop.dao.Cad_SaidasDAO;
import com.sgop.dao.Cad_cadastroDAO;
import com.sgop.fragment.Boleto;
import com.sgop.fragment.Categoria;

import java.util.List;

/**
 * Created by anderson on 11/11/13.
 */
public class Controller {

	private static CadUsuarioDAO cadUsuarioDao;
	private static Cad_BoletoDAO cad_boletoDao;
	private static Cad_EntradaDAO cad_entradaDao;
	private static Cad_SaidasDAO cad_saidaDao;
	private static Cad_cadastroDAO cad_cadastroDAO;
	private static Controller instance;
	public static String usuarioValido;

	public static Controller getInstance(Context context) {
		if (instance == null) {
			instance = new Controller();
			cadUsuarioDao = new CadUsuarioDAO(context);
			cad_boletoDao = new Cad_BoletoDAO(context);
			cad_entradaDao = new Cad_EntradaDAO(context);
			cad_saidaDao = new Cad_SaidasDAO(context);
			cad_cadastroDAO = new Cad_cadastroDAO(context);
		}
		return instance;
	}

	public void insert(Registro usuario) throws Exception {

		cadUsuarioDao.insert(usuario);
	}

	public void update(Registro usuario) throws Exception {
		cadUsuarioDao.update(usuario);
	}

	public void delete(Registro usuario) throws Exception {
		cadUsuarioDao.delete(usuario);
	}

	public List<Registro> findAll() throws Exception {
		return cadUsuarioDao.findAll();
	}

	/*
	 * public Integer UsrNum(String email) throws Exception{ Registro usr_num =
	 * cadUsuarioDao.findNumberUser(email);
	 * 
	 * return usr_num; }
	 */

	public boolean validaLogin(String email, String senha) throws Exception {
		Registro user = cadUsuarioDao.findByLogin(email, senha);
		Log.e("Nome", user.getNome());
		Registro usuario_cod = cadUsuarioDao.RetornaCodigoUsuario(email);
		
		
		if (user == null || user.getEmail() == null || user.getSenha() == null) {
			user = cadUsuarioDao.findByUsuario(email);
			if ( user != null){
			usuarioValido= user.getEmail();
			}
			return false;
		}
		String informado = email + senha;
		String esperado = user.getEmail() + user.getSenha();
		usuarioValido= user.getEmail();
		if (informado.equals(esperado)) {
			Sessao.email_usr = user.getEmail();
			
			// realizar consulta pra retornar o codigo do usuario

			Sessao.codigo_usr = usuario_cod.getcodigo();
			Sessao.nome = user.getNome();
			return true;

		}
		return false;

	}

	public boolean verificaEmail(String email) throws Exception {
		Registro user = cadUsuarioDao.findByUsuario(email);
		
		if (user != null) {
			return false;
		}
		return true;
	}

	public void insertBoleto(Boleto boleto) throws Exception {

		cad_boletoDao.insert(boleto);
	}

	public void updateBoleto(Boleto boleto) throws Exception {
		cad_boletoDao.update(boleto);
	}



	public List<Boleto> findAllBoletos() throws Exception {
		return cad_boletoDao.findAll();
	}

	public static List<String> findStatus() throws Exception {
		return cad_boletoDao.findStatus();
	}

	public void insertEntradas(Entrada entrada) throws Exception {
		cad_entradaDao.insert(entrada);
	}

	public void updateEntradas(Entrada entrada) throws Exception {
		cad_entradaDao.update(entrada);
	}

	public void deleteEntradas(Entrada entrada) throws Exception {
		cad_entradaDao.delete(entrada);
	}

	public List<Entrada> findAllEntradas() throws Exception {
		return cad_entradaDao.findAll();
	}

	public void insertSaidas(Saida saida) throws Exception {
		cad_saidaDao.insert(saida);
	}

	public void updateSaidas(Saida saida) throws Exception {
		cad_saidaDao.update(saida);
	}

	public void deleteSaidas(Saida saida) throws Exception {
		cad_saidaDao.delete(saida);
	}

	public List<Saida> findAllSaidas() throws Exception {
		return cad_saidaDao.findAll();
	}

	public static void insertStatus() throws Exception {
		cad_boletoDao.insertStatus();
	}

	public List<String> findAllStatus() throws Exception {
		return cad_boletoDao.findStatus();
	}

	public void insertCategoria(Categoria categoria) throws Exception {
		cad_cadastroDAO.insert(categoria);
	}

	public List<String> findAllCategorias() throws Exception {
		return cad_saidaDao.findAllCategoria();
		// return cad_saidaDao.getCategorias();
	}

	public boolean verificaCateg(String descricao) throws Exception {
		Categoria cat = (Categoria) cad_cadastroDAO.findByCat(descricao);
		if (cat.descricao == null) {
			return true;
		} else {
			return false;
		}
	}

	public void insertFonteEntrada(FonteEntrada fnt) throws Exception {
		cad_cadastroDAO.insert_Fnt(fnt);
	}

	public List<String> findAllFnt_Entradas() throws Exception {
		return cad_entradaDao.findAllFontes();
	}

	public boolean verificaFnt(String descricao) throws Exception {
		FonteEntrada fnt = (FonteEntrada) cad_cadastroDAO.findAllFnt(descricao);
		if (fnt.descricao == null) {
			return true;
		} else {
			return false;
		}
	}

	public double findByFilter() {
		// TODO Auto-generated method stub
		Double var = 0.0;
		List<String> TotalBoleto = cad_boletoDao.findByFilter();
		List<String> TotalSaidas = cad_saidaDao.findByFilter();
		
		for (int i = 0; i < TotalBoleto.size(); i++) {
			var += Double.parseDouble(TotalBoleto.get(i));
		}

		for (int i = 0; i < TotalSaidas.size(); i++) {
			 TotalSaidas.get(i).replaceAll(",", ".");
			var += Double.parseDouble(TotalSaidas.get(i));
		}

		return var;
	}

public double findByFilterEntradas() {
	// TODO Auto-generated method stub
	Double var = 0.0;	
	List<String> TotalEntradas = cad_entradaDao.findByFilter();	

	for (int i = 0; i < TotalEntradas.size(); i++) {
		var += Double.parseDouble(TotalEntradas.get(i));
	}
	return var;
}
}
