import { useState, useEffect } from 'react';
import './App.css'; // Importa o arquivo de estilos

function App() {

  // Estados para guardar os dados da nossa aplicação
  const [produtos, setProdutos] = useState([]); // Guarda a lista de produtos do backend (começa vazia)
  const [produtoSelecionado, setProdutoSelecionado] = useState(''); // Guarda o ID do produto escolhido
  const [numGotas, setNumGotas] = useState(''); // Guarda o número de gotas digitado
  const [medicamentos, setMedicamentos] = useState(''); // Guarda a string de medicamentos
  
  const [resultado, setResultado] = useState(null); // Guarda o objeto de resposta do cálculo
  const [isLoading, setIsLoading] = useState(false); // Indica se estamos esperando uma resposta da API
  const [error, setError] = useState(''); // Guarda mensagens de erro


  useEffect(() => {
  // Função para buscar os produtos da nossa API
  const fetchProdutos = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/calculadora/produtos');
      if (!response.ok) {
        throw new Error('Erro ao buscar produtos!');
      }
      const data = await response.json();
      setProdutos(data); // Armazena a lista de produtos no nosso estado
    } catch (error) {
      setError('Não foi possível carregar os produtos. O backend está rodando?');
      console.error(error);
    }
  };

  fetchProdutos();
}, []); // rodando apenas uma vez

  
  const handleCalcular = async () => {
  if (!produtoSelecionado || !numGotas) {
    setError('Por favor, selecione um produto e informe o número de gotas.');
    return;
  }
  
  setIsLoading(true);
  setError('');
  setResultado(null);

  // Converte a string de medicamentos em um array, tratando espaços e vírgulas
  const medicamentosArray = medicamentos.split(',').map(med => med.trim()).filter(med => med);

  try {
    const response = await fetch('http://localhost:8080/api/v1/calculadora/calcular', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        produtoId: produtoSelecionado,
        numeroDeGotas: parseInt(numGotas),
        medicamentosAtuais: medicamentosArray,
      }),
    });

    if (!response.ok) {
      throw new Error('Erro ao calcular a dosagem.');
    }

    const data = await response.json();
    setResultado(data); // Salva o resultado no estado
  } catch (error) {
    setError(error.message);
    console.error(error);
  } finally {
    setIsLoading(false); // Para de carregar, independentemente de sucesso ou erro
  }
};

  // Substitua o seu return por este:

return (
  <div className="App">
    <header className="App-header">
      <h1>Calculadora Canabinoide</h1>
    </header>
    <main className="container">
      {/* 1. MOSTRAR MENSAGENS DE ERRO */}
      {/* Este parágrafo só aparece se houver algo no estado 'error' */}
      {error && <p style={{ color: 'red', fontWeight: 'bold' }}>{error}</p>}

      <div className="form-calculadora">
        {/* 2. DROPDOWN DE PRODUTOS DINÂMICO */}
        <div className="form-group">
          <label htmlFor="produto-select">Selecione o Produto:</label>
          <select 
            id="produto-select" 
            name="produto"
            value={produtoSelecionado} // O valor exibido é o que está no nosso estado
            onChange={(e) => setProdutoSelecionado(e.target.value)} // Quando muda, atualiza o estado
          >
            <option value="">-- Escolha um óleo --</option>
            {/* Usamos .map() para criar uma <option> para cada produto na lista */}
            {produtos.map((produto) => (
              <option key={produto.id} value={produto.id}>
                {produto.nome}
              </option>
            ))}
          </select>
        </div>

        {/* 3. INPUT DE GOTAS CONECTADO AO ESTADO */}
        <div className="form-group">
          <label htmlFor="gotas-input">Número de Gotas:</label>
          <input 
            id="gotas-input" 
            type="number" 
            min="1" 
            placeholder="Ex: 5"
            value={numGotas} // O valor exibido é o que está no estado
            onChange={(e) => setNumGotas(e.target.value)} // Quando o usuário digita, atualiza o estado
          />
        </div>

        {/* 4. INPUT DE MEDICAMENTOS CONECTADO AO ESTADO */}
        <div className="form-group">
          <label htmlFor="medicamentos-input">Medicamentos em Uso (separados por vírgula):</label>
          <input 
            id="medicamentos-input" 
            type="text" 
            placeholder="Ex: Varfarina, Clobazam..."
            value={medicamentos} // O valor exibido é o que está no estado
            onChange={(e) => setMedicamentos(e.target.value)} // Quando o usuário digita, atualiza o estado
          />
        </div>

        {/* 5. BOTÃO DE AÇÃO CONECTADO À FUNÇÃO */}
        <button 
          className="calcular-btn" 
          onClick={handleCalcular} // Chama a função handleCalcular quando clicado
          disabled={isLoading} // Desabilita o botão enquanto está carregando
        >
          {isLoading ? 'Calculando...' : 'Calcular Dosagem'}
        </button>
      </div>

      {/* 6. ÁREA DE RESULTADOS CONDICIONAL */}
      {/* Esta seção inteira só aparece se o 'resultado' não for nulo */}
      {resultado && (
        <div className="resultados">
          <h2>Resultados para {numGotas} gotas:</h2>
          <div id="resultado-dosagem">
            {/* Usamos .map() para mostrar a dosagem de cada canabinoide */}
            {resultado.dosagens.map((item) => (
              <p key={item.nomeCanabinoide} className="dosagem-item">
                <span style={{ fontWeight: 'bold' }}>{item.nomeCanabinoide}:</span> {item.miligramas.toFixed(2)} mg
              </p>
            ))}
          </div>
          <div id="resultado-interacao">
            <h3>Aviso de Interação Medicamentosa</h3>
            <p>{resultado.avisoInteracao}</p>
          </div>
        </div>
      )}
    </main>
  </div>
);
}

export default App;