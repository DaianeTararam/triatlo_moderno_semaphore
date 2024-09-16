//@author Daiane Tararam

package controller;
import java.util.concurrent.Semaphore;

// Corrida de 3000m - atletas correm entre 20 e 25m/ a cada 30ms
// Tiro ao alvo - pontuação de 0 a 10 com 3 chances
// Cada atleta leva de 0.5 a 3 segundos por tiro
// Ciclismo de 5000m - atletas pedalam entre 30 e 40m/ a cada 40ms

public class TriatloModerno extends Thread {
	private int atleta;
	private int velocidadeCorrida = 20 + (int) (Math.random() * 6);
	private int velocidadeCiclismo = 30 + (int) (Math.random() * 11);
	private static Semaphore armas = new Semaphore(5);
	private static Semaphore ranking = new Semaphore(1);
	private int pontosTiroAlvo = 0;
	private static int posicaoChegada = 250;
	private static int[][] pontosAtletas = new int[25][2];
	private static int atletasFinalizados = 0;
	
	public TriatloModerno(int atleta) {
		this.atleta = atleta;
	}

	@Override
	public void run() {
		corrida();
		tiroAoAlvo();
		corridaCiclismo();
		pontuacao();
	}

	private void corrida() {
		int distancia = 0;
        System.out.println("O atleta " + atleta + " iniciou a corrida.");
        try {
            while (distancia < 3000) {
                sleep(30);
                distancia += velocidadeCorrida;
            }
            System.out.println("O atleta " + atleta + " COMPLETOU a corrida.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void tiroAoAlvo() {
		System.out.println("O atleta " + atleta + " iniciou o tiro ao alvo.");
		try {
			armas.acquire();
			for (int i = 1; i <= 3; i++) {
				pontosTiroAlvo += (int) (Math.random() * 11);
				sleep((int) ((Math.random() * 2.5 + 0.5) * 1000));
			}
			System.out.println("O atleta " + atleta + " fez " + pontosTiroAlvo + " pontos no Tiro ao Alvo.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			armas.release();
		}
	}

	private void corridaCiclismo() {
		System.out.println("O atleta " + atleta + " pegou a sua bicicleta e iniciou o circuito.");
		int distancia = 0;
		try {
			while (distancia < 5000) {
				sleep(40);
				distancia += velocidadeCiclismo;
			}
			System.out.println("O atleta " + atleta + " COMPLETOU o circuito!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void pontuacao() {
		 int pontos = 0;
	        try {
	            ranking.acquire();
	            if (posicaoChegada >= 10) {
	                pontos = posicaoChegada + pontosTiroAlvo;
	                pontosAtletas[atleta - 1][0] = atleta;
	                pontosAtletas[atleta - 1][1] = pontos;
	                posicaoChegada -= 10;
	            }
	            atletasFinalizados++;
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        } finally {
	            ranking.release();
	        }
	        if (atletasFinalizados == 25) {
	            ordenar();
	            impressao();
	        }
	}

	private void ordenar() {
		for (int i = 0; i < pontosAtletas.length - 1; i++) {
            for (int j = 0; j < pontosAtletas.length - 1 - i; j++) {
                if (pontosAtletas[j][1] < pontosAtletas[j + 1][1]) {
                    int[] aux = pontosAtletas[j];
                    pontosAtletas[j] = pontosAtletas[j + 1];
                    pontosAtletas[j + 1] = aux;
                }
            }
        }
	}

	private void impressao() {
		int pontuacao = 250;
        System.out.println("=== Ranking Final ===");
        for (int i = 0; i < pontosAtletas.length; i++) {
            System.out.println("O atleta " + pontosAtletas[i][0] + " teve " + pontuacao + " pontos");
            pontuacao -= 10;
        }
    }
}
