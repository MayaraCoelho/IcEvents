package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Evento;
import model.Palestrante;
import model.Pessoa;
import model.Reserva;
import model.TipoEvento;
import persistence.EventoDao;

@WebServlet("/EventoController")
public class EventoController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private EventoDao daoEvento;
	
    public EventoController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		verificarUrl(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		verificarUrl(request, response);
	}
	
	protected void verificarUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getServletPath();
		
		if (daoEvento == null) {
			daoEvento = new EventoDao();
		}
		
//		if (url.equals("/evento/carregaCadastro")){
//			carregarCadastroEvento(request, response);
//		}
		
		if (url.equals("/evento/cadastro")){
			cadastrarEvento(request, response);
		}
		
		if (url.equals("/evento/busca")){
			consultarEvento(request, response);
		}
		
		if (url.equals("/evento/lista")){
			listarEventos(request, response);
		}
		
		if (url.equals("/evento/atualizar")){
			atualizarEvento(request, response);
		}
		
		if (url.equals("/evento/remover")){
			removerEvento(request, response);
		}
		
	}
	
//	protected void carregarCadastroEvento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		HttpSession sessao = request.getSession();
//		
//		try {
//			
//			List<Evento> eventos = new ArrayList<Evento>();
//			eventos = daoEvento.listarTodos();
//			request.setAttribute("listaEventos", eventos);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		request.getRequestDispatcher("../cadastroSala.xhtml").forward(request, response);
//	}

	protected void cadastrarEvento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sessao = request.getSession();
		
		List<Reserva> reservas = new ArrayList<Reserva>();
		List<Palestrante> palestrantes = new ArrayList<Palestrante>();
		List<Pessoa> participantes = new ArrayList<Pessoa>();
		List<Pessoa> organizadores = new ArrayList<Pessoa>();

		try {
			
			Evento ev = new Evento(null, request.getParameter("tema"), request.getParameter("descricao"), Integer.parseInt(request.getParameter("vagas")));
			ev.setTipoEvento(TipoEvento.valueOf(request.getParameter("tipoEvento")));
			
			ev.setParticipantes(participantes);
			ev.setOrganizadores(organizadores);
			ev.setPalestrantes(palestrantes);
			ev.setReservas(reservas);
			
			daoEvento.inserir(ev);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.sendRedirect("lista");
	}
	
	protected void consultarEvento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sessao = request.getSession();
		
		List<Reserva> reservas = new ArrayList<Reserva>();
		List<Palestrante> palestrantes = new ArrayList<Palestrante>();
		List<Pessoa> participantes = new ArrayList<Pessoa>();
		List<Pessoa> organizadores = new ArrayList<Pessoa>();
		
		try {
		
			Integer codigo = Integer.parseInt(request.getParameter("evento"));
			Evento ev = daoEvento.buscarPorId(codigo);
			
			ev.setParticipantes(participantes);
			ev.setOrganizadores(organizadores);
			ev.setPalestrantes(palestrantes);
			ev.setReservas(reservas);
			
			request.setAttribute("evento", ev);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("../listarEvento_Admin.xhtml").forward(request, response);
	}
	
	protected void listarEventos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		
			List<Evento> Eventos = new ArrayList<Evento>();
			Eventos = daoEvento.listarTodos();
			request.getSession().setAttribute("listaEventos", Eventos);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.sendRedirect("../listarEventos.xhtml");
	}
	
	protected void atualizarEvento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Evento ev = new Evento(Integer.parseInt(request.getParameter("evento")), request.getParameter("tema"), request.getParameter("descricao"), Integer.parseInt(request.getParameter("vagas")));
		ev.setTipoEvento(TipoEvento.valueOf(request.getParameter("tipoEvento")));
		
		try {
			
			daoEvento.atualizar(ev);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("lista").forward(request, response);
	}

	protected void removerEvento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Integer codigo = Integer.parseInt(request.getParameter("evento"));
			Evento ev = daoEvento.buscarPorId(codigo);
			daoEvento.remover(ev);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("lista").forward(request, response);
	}

}
