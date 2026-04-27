/**
 * Componente ChatBox
 * Exibe o histórico de mensagens e input para nova pergunta.
 */
import React, { useEffect, useRef } from 'react';
import '../styles/ChatBox.css';

function ChatBox({ messages, isLoading, error, onSendMessage, onClear }) {
  const messagesEndRef = useRef(null);

  // Scroll automático para a última mensagem
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const input = e.target.elements.pergunta;
    const pergunta = input.value;

    if (pergunta.trim()) {
      onSendMessage(pergunta);
      input.value = '';
      input.focus();
    }
  };

  return (
    <div className="chat-box">
      {/* Header do Chat */}
      <div className="chat-header">
        <h3>🤖 Assistente de Cannabis</h3>
        <button
          className="chat-clear-btn"
          onClick={onClear}
          title="Limpar conversa"
        >
          🔄
        </button>
      </div>

      {/* Área de Mensagens */}
      <div className="messages-container">
        {messages.length === 0 ? (
          <p className="empty-message">
            👋 Olá! Sou seu assistente de IA. Faça perguntas sobre cannabis medicinal.
          </p>
        ) : (
          messages.map((msg) => (
            <div
              key={msg.id}
              className={`message ${msg.tipo === 'usuario' ? 'usuario' : 'agente'}`}
            >
              <div className="message-content">
                <p>{msg.texto}</p>
                {msg.tipo === 'agente' && (
                  <small className="message-source">{msg.fonte}</small>
                )}
              </div>
            </div>
          ))
        )}

        {isLoading && (
          <div className="message agente loading">
            <div className="message-content">
              <p>
                <span className="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </span>
              </p>
            </div>
          </div>
        )}

        {error && (
          <div className="message error">
            <div className="message-content">
              <p>❌ {error}</p>
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Input de Pergunta */}
      <form onSubmit={handleSubmit} className="chat-form">
        <input
          type="text"
          name="pergunta"
          placeholder="Faça uma pergunta..."
          disabled={isLoading}
          className="chat-input"
          autoComplete="off"
        />
        <button
          type="submit"
          disabled={isLoading}
          className="chat-send-btn"
          title="Enviar pergunta"
        >
          {isLoading ? '...' : '📤'}
        </button>
      </form>
    </div>
  );
}

export default ChatBox;
