package controller;

import java.io.IOException;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Pessoa;
import persistence.ControleAcessoDAO;
import sun.misc.BASE64Encoder; 

@WebServlet("/ControleAcesso")
public class ControleAcesso extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ControleAcessoDAO daoControleAcesso;
	
    public ControleAcesso() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		verificarUrl(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		verificarUrl(request, response);
	}
	
	protected void verificarUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = request.getServletPath();
		
		if (daoControleAcesso == null) {
			daoControleAcesso = new ControleAcessoDAO();
		}

		if (url.equals("/login")){
			autenticarUsuario(request, response);
		}
		
		if (url.equals("/logout")){
			logoutUsuario(request, response);
		}
		
	}
	
	protected void autenticarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String senha = request.getParameter("senha");
		
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] crip = md.digest(senha.getBytes());
			BASE64Encoder enc = new BASE64Encoder();
			
			Pessoa p = daoControleAcesso.buscarPorEmailSenha(email, enc.encode(crip));
			
			if (p!=null) {
				
				HttpSession sessao = request.getSession();
				sessao.setAttribute("user", p); 
				
			} else {
				
				request.setAttribute("msg", "Login ou senha incorretos!");
				response.sendRedirect("login.jsp");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "Erro: " + e.getMessage());
		}
		
		response.sendRedirect("index.xhtml");
		
	}

	protected void logoutUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sessao = request.getSession();
		
		try {
			
			if (sessao != null) {
				
				sessao.invalidate();
			
			} else {
				
				request.setAttribute("msg", "Erro ao fazer logoff");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "Erro: " + e.getMessage());
		}
		
		response.sendRedirect("index.xhtml");
		
	}
}
