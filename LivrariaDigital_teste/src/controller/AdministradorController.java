/**
 * @author Fernando Moraes Oliveira
 * Matéria 4724 - Engenharia de Software 3
 * 4º ADS - Noite
 * Iniciado em 03/05/2016
 */

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import boundary.FrmAdministrador;
import boundary.FrmPrincipal;
import dao.ArquivoAdministrador;
import entity.Administrador;


public class AdministradorController implements ComponentListener {

	private FrmAdministrador janela;
	private JPanel painel;
	private FrmPrincipal janelaPrincipal;
	private JTextField txtId, txtUsuario;
	private JPasswordField pwdSenha, pwdSenha2;
	private JCheckBox chckbxAdm, chckbxOpera;
	private JButton btnEntrar; 
	//private JButton btnCadastrar;
	private List<Administrador> usuarios;
	private static int contador = 1;
	private boolean validar;
	private String diretorio = "../LivrariaDigital_teste/";
	private String arquivo = "administrador";
	private ArquivoAdministrador dao = new ArquivoAdministrador();
	private SessaoController logon = SessaoController.getInstance();

	public AdministradorController (
			FrmAdministrador janela, 
			JPanel painel, 
			JTextField txtId, 
			JTextField txtUsuario, 
			JPasswordField pwdSenha, 
			JPasswordField pwdSenha2, 
			JCheckBox chckbxAdm, 
			JCheckBox chckbxOpera, 
			JButton btnCadastrar, 
			JButton btnEntrar
			) {

		this.janela = janela;
		this.painel = painel;
		this.txtId = txtId;
		this.txtUsuario = txtUsuario;
		this.pwdSenha = pwdSenha;
		this.pwdSenha2 = pwdSenha2;
		this.chckbxAdm = chckbxAdm;
		this.chckbxOpera = chckbxOpera;
		//this.btnCadastrar = btnCadastrar;
		this.btnEntrar = btnEntrar;
		this.usuarios = new ArrayList<Administrador>();

		lerArquivo();
	}
	


	// METODOS DE SUPORTE ////////////////////////


	public void gerarId(){

		DateFormat dateFormat = new SimpleDateFormat("yyMMdd-HHmmss");
		Date date = new Date();
		String NewId = (dateFormat.format(date));
		txtId.setText("USR" + NewId);
	}


	public void limpaCampos() {

		txtId.setText(null);
		txtUsuario.setText(null);
		pwdSenha.setText(null);
		pwdSenha2.setText(null);
		chckbxAdm.setSelected(false);
		chckbxOpera.setSelected(false);
	}


	public boolean validar(String validaSenha) {  

		if (validaSenha != null){
			char[] senha = pwdSenha.getPassword();
			if (senha.length != validaSenha.length()) {  
				return false;
			} else {  
				for (int i = 0; i < senha.length; i++) {   
					if (senha[i] != validaSenha.charAt(i)) {  
						return false; 
					}  
				}  
			}  
			return true; 
		}
		return false;
	} 
	
	
	public void abrirJanela ( String nome ){
		
		switch ( nome ){

		case "principal":
			if( janelaPrincipal == null ){
				FrmPrincipal principal;
				principal = new FrmPrincipal();
				principal.setVisible(true);
			} else {
				janelaPrincipal.setVisible(true);
			}
			break;
		}
	}


	// CRUD //////////////////////////


	public void entrar() {

		String n = "";
		if (!txtUsuario.getText().isEmpty() 
				&& pwdSenha.getPassword().length != 0) {
			for (int i = 0; i < usuarios.size(); i++) {
				if (txtUsuario.getText().equalsIgnoreCase(usuarios.get(i).getUsuario())
						&& validar(usuarios.get(i).getSenha()) == true) {
					txtId.setText(usuarios.get(i).getId());
					if (("Administrador").equalsIgnoreCase(usuarios.get(i).getNivel())){
						chckbxAdm.setSelected(true);
						//btnCadastrar.setVisible(true);
						n = chckbxAdm.getText();
					} else if (("Operacional").equalsIgnoreCase(usuarios.get(i).getNivel())){
						chckbxOpera.setSelected(true);
						//btnCadastrar.setVisible(false);
						n = chckbxOpera.getText();
					}
					logon.registrar(txtId.getText(), txtUsuario.getText(), n, painel.getName());
//					msg("pwd", txtUsuario.getText());
					validar = true;
					fechar();
				} 			
			}				
			if (validar == false){
				msg("errorpwd", txtUsuario.getText());
				//limpaCampos();
			} 
		} else {	
			msg("errornull", txtUsuario.getText());
		}
		validar = false;
	}


	public void acesso() {

		if (!txtUsuario.getText().isEmpty() 
				&& pwdSenha.getPassword().length != 0) {
			logon.carregar();
			if (("Administrador").equalsIgnoreCase(
					logon.registrar(
							txtId.getText(), 
							txtUsuario.getText(), 
							chckbxAdm.getText(), 
							painel.getName()
							))){	

				//FrmLogin frame = new FrmLogin();
				//frame.dispose(); //NAO FECHA O FRMLOGIN!

				FrmAdministrador janelaUsuario = new FrmAdministrador();
				janelaUsuario.setVisible(true);
			} else {
				msg("errorsession", "");
			}
		} else {
			msg("errornull", "");
		}
	}


	public void pesquisar() {

		ArrayList<Administrador> lista = new ArrayList<>();
		String pesquisa ="";
		if (!txtUsuario.getText().isEmpty() || !txtId.getText().isEmpty()) {
			for (int i = 0; i < usuarios.size(); i++) {
				if (txtUsuario.getText().equalsIgnoreCase(usuarios.get(i).getId())) {
					txtId.setText(usuarios.get(i).getId());
					txtUsuario.setText(usuarios.get(i).getUsuario());
					if (("Administrador").equalsIgnoreCase(usuarios.get(i).getNivel())){
						chckbxAdm.setSelected(true);
					} else if (("Operacional").equalsIgnoreCase(usuarios.get(i).getNivel())){
						chckbxOpera.setSelected(true);
					}
					validar = true;
				} else if (usuarios.get(i).getUsuario().toLowerCase().startsWith(txtUsuario.getText().toLowerCase())) {
					validar = true;
				}
			}
			if (validar == true) {
				for (int i = 0; i < usuarios.size(); i++) {

					boolean filtro = usuarios.get(i).getUsuario().toLowerCase().startsWith(txtUsuario.getText().toLowerCase());
					if (filtro == true) {
						Administrador item = new Administrador();
						item.setId(usuarios.get(i).getId());
						item.setUsuario(usuarios.get(i).getUsuario());
						item.setSenha(usuarios.get(i).getSenha());
						item.setNivel(usuarios.get(i).getNivel());
						lista.add(item);
					}
				}
				String[] filtro = new String[lista.size()];
				for (int i = 0; i < lista.size(); i++) {
					filtro[i] = lista.get(i).getId() + " : " + lista.get(i).getUsuario();
					pesquisa = lista.get(i).getId();
				}
				if (filtro != null && filtro.length > 1) {
					pesquisa = (String) JOptionPane.showInputDialog(janela, "Selecione:\n", "Registros Localizados",
							JOptionPane.INFORMATION_MESSAGE, null, filtro, filtro[0]);
				}
				for (int i = 0; i < usuarios.size(); i++) {
					if (pesquisa.replaceAll(" : .*", "").equalsIgnoreCase(usuarios.get(i).getId())) {
						txtId.setText(usuarios.get(i).getId());
						txtUsuario.setText(usuarios.get(i).getUsuario());
						if (("Administrador").equalsIgnoreCase(usuarios.get(i).getNivel())){
							chckbxAdm.setSelected(true);
						}else if (("Operacional").equalsIgnoreCase(usuarios.get(i).getNivel())){
							chckbxOpera.setSelected(true);
						}
					}
				}
				validar = false; 
			} else {
				if (pesquisa == "") {
					msg("nosearch", txtUsuario.getText());
					limpaCampos();
				}
				validar = false;
			}
		} else {
			msg("errorsearch", txtUsuario.getText());
		}
	}


	@SuppressWarnings("deprecation")
	public void editar() {

		Administrador usuario = new Administrador();
		validar = false;
		if (!txtId.getText().isEmpty()
				&& pwdSenha.getPassword().length != 0) {
			for (int i = 0; i < usuarios.size(); i++) {
				if (!txtId.getText().equalsIgnoreCase(usuarios.get(i).getId()) 
						&& txtUsuario.getText().equalsIgnoreCase(usuarios.get(i).getUsuario())) {				
					msg("erroredit",usuarios.get(i).getUsuario());
					validar = true;
				} 
			}
			if(!(validar == true)){
				for (int i = 0; i < usuarios.size(); i++) {
					if (txtId.getText().equalsIgnoreCase(usuarios.get(i).getId())) {
						usuario.setId(txtId.getText());
						usuario.setUsuario(txtUsuario.getText());
						usuario.setSenha(pwdSenha.getText().toString());
						if(chckbxAdm.isSelected()){
							usuario.setNivel(chckbxAdm.getText());
						}else if(chckbxOpera.isSelected()){
							usuario.setNivel(chckbxOpera.getText());
						}
						usuarios.set(i, usuario);
						atualizarDados(usuarios);
						msg("edit", txtUsuario.getText());
						limpaCampos();
					}
				}
			}
		} else {
			msg("errornull", txtUsuario.getText());
		}
	}

	public void excluir() {

		if (!txtUsuario.getText().isEmpty()) {
			for (int i = 0; i < usuarios.size(); i++) {
				if (txtId.getText().equalsIgnoreCase(usuarios.get(i).getId()) 
						&& txtUsuario.getText().equalsIgnoreCase(usuarios.get(i).getUsuario())) {
					usuarios.remove(i);
					validar = true;
				}
			}
			if (validar == true) {
				msg("deleteconfirm", txtUsuario.getText());
				if (validar == false){
					atualizarDados(usuarios);
					msg("delete", txtUsuario.getText());
					limpaCampos();
				} else {
					usuarios.clear();
					lerArquivo();	
				}
			} else {
				validar = false;
				msg("errordelete", txtId.getText());
			}
		} else {
			pesquisar();
		}
	}


	@SuppressWarnings("deprecation")
	public void salvar() {

		gerarId();
		new ArquivoAdministrador();
		Administrador usuario = new Administrador();
		validar = false;
		if (!txtUsuario.getText().isEmpty() 
				&& pwdSenha.getPassword().length != 0) {
			for (int i = 0; i < usuarios.size(); i++) {
				if (txtUsuario.getText().equalsIgnoreCase(usuarios.get(i).getUsuario())){
					msg("erroredit", usuarios.get(i).getUsuario());
					validar = true;
				}
			}
			if(!(validar == true)){
				if( pwdSenha.getText().equals(pwdSenha2.getText() )){
					usuario.setId(txtId.getText());
					usuario.setUsuario(txtUsuario.getText());
					usuario.setSenha(pwdSenha.getText().toString());
					if(chckbxAdm.isSelected()){
						usuario.setNivel(chckbxAdm.getText());
					}else if(chckbxOpera.isSelected()){
						usuario.setNivel(chckbxOpera.getText());
					}
					usuarios.add(usuario);
					msg("save", txtUsuario.getText());
					atualizarDados(usuarios);
					limpaCampos();
					gerarId();
				} else {
					msg("errorpwd2", txtUsuario.getText());
				}
			}
		} else {
			msg("errornull", txtUsuario.getText());
		}
	}
	
	
	public void lerArquivo() {

		String linha = new String();
		ArrayList<String> list = new ArrayList<>();

		try {
			dao.lerArquivo( diretorio + "dados/", arquivo );
			linha = dao.getBuffer();
			String[] listaUsuario = linha.split(";");
			for (String s : listaUsuario) {
				String text = s.replaceAll(".*: ", "");
				list.add(text);
				if (s.contains("---")) {
					Administrador usuario = new Administrador();
					usuario.setId(list.get(0));
					usuario.setUsuario(list.get(1));
					usuario.setSenha(list.get(2));
					usuario.setNivel(list.get(3));
					usuarios.add(usuario);
					list.clear();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void atualizarDados( List<Administrador> listaUsuario ) {

		File f = new File( diretorio + "dados/", arquivo );
		f.delete();	
		for ( Administrador usuarios : listaUsuario ) {
			try {
				dao.escreverArquivo( diretorio  + "dados/", arquivo, "", usuarios );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	// MENSAGENS //////////////////////////////

	public void msg(String tipo, String mensagem) {

		switch (tipo) {

		case "errornull":
			JOptionPane.showMessageDialog(null, 
					"ATENÇÃO!\nCampo Vazio.\n\nPor favor, digite o Usuário e a Senha.", 
					"Erro", 
					JOptionPane.PLAIN_MESSAGE,
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "errorsession":
			JOptionPane.showMessageDialog(null, 
					"ACESSO NEGADO!\n\nPor favor, solicite a autorização de um administrador.", 
					"Bloqueado", 
					JOptionPane.PLAIN_MESSAGE,
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "nosearch":
			JOptionPane.showMessageDialog(null, 
					"ATENÇÃO!\n\nNão localizamos o usuário: '" + mensagem + "' !\nVerifique sua digitação.", 
					"Não Localizado", 
					JOptionPane.PLAIN_MESSAGE,
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "errorsearch":
			JOptionPane.showMessageDialog(null, 
					"ATENÇÃO! Por favor, digite para pesquisar!", 
					"Erro",
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "save":
			JOptionPane.showMessageDialog(null, 
					"Usuário '" + mensagem + "' salvo com sucesso.", 
					"Confirmação", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/record.png" ));
			break;
		case "errorrec":
			JOptionPane.showMessageDialog(null, 
					"ATENÇÃO!\nNão foi possível apagar o usuário: " 
							+ txtUsuario.getText() + "!\nVerifique sua digitação!", 
							"Erro", 
							JOptionPane.PLAIN_MESSAGE,
							new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "edit":
			JOptionPane.showMessageDialog(null, 
					"Usuário '" + mensagem + "' editado com sucesso.", 
					"Confirmação", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/record.png" ));
			break;
		case "erroredit":
			JOptionPane.showMessageDialog(null, 
					"Usuário '" + mensagem + "' já existe!",
					"Já Cadastrado", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "deleteconfirm":
			Object[] options = { "Confirmar", "Cancelar" };  
			int r = JOptionPane.showOptionDialog(null, "Você confirma a exclusão do usuário '" + mensagem + "'?",
					"Exclusão de Registro", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
					new ImageIcon( diretorio + "/icons/warning.png" ), options, options[0]);
			if (r == 0) {
				validar = false;
			}
			break;
		case "delete":
			JOptionPane.showMessageDialog(null, 
					"Usuário '" + mensagem + "' excluído com sucesso.", 
					"Confirmação", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/record.png" ));
			break;
		case "errordelete":
			JOptionPane.showMessageDialog(null, 
					"O usuário '" + mensagem + "' não pode ser alterado para a exclusão.",
					"Erro", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "pwd":
			JOptionPane.showMessageDialog(null, 
					"Seja bem vindo ao sistema " + mensagem + "!", 
					"Confirmação", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/ok.png" ));
			break;
		case "errorpwd":
			JOptionPane.showMessageDialog(null, 
					"O usuário '" + mensagem + "' ou sua senha não conferem!\n\nVerifique sua digitação e tente novamente.",
					"Erro", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		case "errorpwd2":
			JOptionPane.showMessageDialog(null, 
					"A senha e a confirmação estão diferentes para o usuário '" + mensagem + "' !\n\nVerifique sua digitação e tente novamente.",
					"Erro", 
					JOptionPane.PLAIN_MESSAGE, 
					new ImageIcon( diretorio + "/icons/warning.png" ));
			break;
		default:
			JOptionPane.showMessageDialog(null, 
					"OOPS!\n\nQue feio, Ed Stark perdeu a cabeça, e algo não deveria ter acontecido…\n\nTermo: " + mensagem
					+ "\n\nVolte ao trabalho duro e conserte isso!!!", 
					"Erro no Controller", 
					JOptionPane.PLAIN_MESSAGE,
					new ImageIcon( diretorio + "/icons/warning.png" ));
		}
	}
	
	
	//  COMPORTAMENTO JANELA  //////////////////////////

	public void fechar(){
		if(janela != null)
			janela.dispose();
	}

	public void mostrar(){
		if(janela != null)
			janela.setVisible(true);
	}

	public void esconder(){
		if(janela != null)
			janela.setVisible(false);
	}
	
	public void sair(){
		msg("sistema","Fechamento");
		if(validar == true){
			System.exit(0);
		}
	}


	// CONTROLE BOTAO //////////////////////////////


	public ActionListener cadastrar = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			acesso();
		}
	};

	public ActionListener entrar = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			entrar();
		}
	};

	public ActionListener pesquisar = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			pesquisar();
		}
	};

	public ActionListener excluir = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			excluir();
		}
	};

	public ActionListener editar = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			editar();
		}
	};

	public ActionListener gravar = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			salvar();
		}
	};
	
	public ActionListener janelas = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			//verifica qual botãoo está solicitando a ação
			Object source = e.getSource();

			if(source == btnEntrar){
				abrirJanela( "principal" );
			}
		}
	};
	
	// CONTROLE MOUSE ///////////////////////////////


	public MouseListener limpaCampo = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if (contador == 1) {
				txtUsuario.setText(null);
				contador += 1;
			}
		}
	};

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
}
