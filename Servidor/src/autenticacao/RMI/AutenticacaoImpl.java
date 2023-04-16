package autenticacao.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class AutenticacaoImpl extends UnicastRemoteObject implements Autenticacao {

    // Lista de Usuários
    private List<Usuario> usuarios = new ArrayList<>();

    // Lista de Objetos
    private ArrayList<String> objetos = new ArrayList<>();


    public AutenticacaoImpl() throws RemoteException {
    }

    // Método que cadastra novo usuário
    @Override
    public boolean registraUsuario(String nome, String senha, String permissao) throws RemoteException {
        try {
            // Verifica se o usuário já existe
            if (getUsuario(nome) == null) {
                usuarios.add(new Usuario(nome, senha, permissao));
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Erro ao registrar usuário na classe cliente");
            e.printStackTrace();
            return false;
        }
    }

    // Método que autentica um usuário e o autoriza a operar um objeto
    @Override
    public String solicitaAcesso(String nome, String senha, String objeto, String operacao) throws RemoteException {
        try {
            // Verifica se o usuário existe e se a senha está correta
            Usuario usuario = getUsuario(nome);
            if (usuario != null) {
                if (usuario.getSenha() != null && usuario.getSenha().equals(senha)) {
                    if (!operacao.equals("") && usuario.getAutorizacao().contains(operacao)) {
                        if (!objetos.contains(objeto)) {
                            if (operacao.equals("escrever")) {
                                objetos.add(objeto);
                                return "Objeto criado!";
                            } else {
                                return "Objeto não existe!";
                            }
                        }
                        return "Acesso concedido!";
                    } else {
                        return "Acesso negado!";
                    }
                }
            }
            return "Usuário não encontrado!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao solicitar acesso!";
        }
    }

    public void listaUsuarios() throws RemoteException {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado");
        } else {
            System.out.println();
            for (Usuario usuario : usuarios) {
                System.out.println("--------------------------------");
                System.out.println("Nome: " + usuario.getNome());
                System.out.println("Senha: " + usuario.getSenha());
                System.out.println("Permissão: " + usuario.getAutorizacao());
            }
            System.out.println("--------------------------------\n");
        }
    }

    public void listaObjetos() throws RemoteException {
        if (objetos.isEmpty()) {
            System.out.println("Nenhum objeto cadastrado");
        } else {
            System.out.println();
            for (String obj : objetos) {
                System.out.println("--------------------------------");
                System.out.println("Nome: " + obj);
            }
            System.out.println("--------------------------------\n");
        }

    }

    private Usuario getUsuario(String nome) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNome().equals(nome)) {
                return usuario;
            }
        }
        return null;
    }
}
