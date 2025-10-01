import { useState, useEffect } from 'react';
import './App.css'; // Importa o arquivo de estilos

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Calculadora Canabinoide</h1>
      </header>
      <main className="container">
        <div className="form-calculadora">
          {/* Seção de Seleção do Produto */}
          <div className="form-group">
            <label htmlFor="produto-select">Selecione o Produto:</label>
            <select id="produto-select" name="produto">
              <option value="">Carregando produtos...</option>
            </select>
          </div>

          {/* Seção de Entrada de Gotas */}
          <div className="form-group">
            <label htmlFor="gotas-input">Número de Gotas:</label>
            <input id="gotas-input" type="number" min="1" placeholder="Ex: 5" />
          </div>

          {/* Seção de Medicamentos */}
          <div className="form-group">
            <label htmlFor="medicamentos-input">Medicamentos em Uso (separados por vírgula):</label>
            <input id="medicamentos-input" type="text" placeholder="Ex: Varfarina, Clobazam..." />
          </div>

          {/* Botão de Ação */}
          <button className="calcular-btn">Calcular Dosagem</button>
        </div>

        {/* --- Área de Resultados --- */}
        <div className="resultados">
          <h2>Resultados</h2>
          <div id="resultado-dosagem">
            {/* O resultado da dosagem aparecerá aqui */}
            <p>Aguardando cálculo...</p>
          </div>
          <div id="resultado-interacao">
            <h3>Aviso de Interação Medicamentosa</h3>
            <p>Nenhuma informação.</p>
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;