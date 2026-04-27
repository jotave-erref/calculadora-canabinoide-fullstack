/**
 * Componente ChatWidget
 * Widget flutuante no canto inferior direito que gerencia a abertura/fechamento do chat.
 */
import React, { useState } from 'react';
import ChatBox from './ChatBox';
import useChatAPI from '../hooks/useChatAPI';
import '../styles/ChatWidget.css';

function ChatWidget() {
  const [isOpen, setIsOpen] = useState(false);
  const { messages, isLoading, error, enviarPergunta, limparHistorico } = useChatAPI();

  const toggleChat = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div className="chat-widget">
      {/* Botão Flutuante (sempre visível) */}
      <button
        className="chat-widget-button"
        onClick={toggleChat}
        title={isOpen ? 'Fechar chat' : 'Abrir chat'}
        aria-label="Abrir assistente de IA"
      >
        <span className="chat-icon">💬</span>
        {messages.length > 0 && !isOpen && (
          <span className="chat-badge">{messages.filter(m => m.tipo === 'agente').length}</span>
        )}
      </button>

      {/* Caixa de Chat (expandida quando isOpen = true) */}
      {isOpen && (
        <div className="chat-widget-container">
          <ChatBox
            messages={messages}
            isLoading={isLoading}
            error={error}
            onSendMessage={enviarPergunta}
            onClear={limparHistorico}
          />
        </div>
      )}
    </div>
  );
}

export default ChatWidget;
