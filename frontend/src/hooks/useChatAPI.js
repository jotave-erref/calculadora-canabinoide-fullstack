/**
 * Hook customizado para integração com a API de Chat do backend.
 * Gerencia requisições, cache e estado do chat.
 */
import { useState, useCallback } from 'react';

const useChatAPI = () => {
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const API_URL = 'http://localhost:8080/api/v1/chat/ask';

  /**
   * Envia uma pergunta para o agente de IA e retorna a resposta.
   * @param {string} pergunta - A pergunta do usuário
   */
  const enviarPergunta = useCallback(async (pergunta) => {
    if (!pergunta.trim()) {
      setError('Por favor, digite uma pergunta.');
      return;
    }

    // Adiciona a pergunta do usuário ao histórico imediatamente
    const novaMensagemUsuario = {
      id: Date.now(),
      tipo: 'usuario',
      texto: pergunta.trim(),
      timestamp: new Date(),
    };

    setMessages((prev) => [...prev, novaMensagemUsuario]);
    setIsLoading(true);
    setError('');

    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ pergunta: pergunta.trim() }),
      });

      if (!response.ok) {
        throw new Error('Erro ao comunicar com o agente de IA.');
      }

      const data = await response.json();

      // Adiciona a resposta do agente ao histórico
      const novaMensagemAgente = {
        id: Date.now() + 1,
        tipo: 'agente',
        texto: data.resposta,
        fonte: data.fonte,
        timestamp: new Date(),
      };

      setMessages((prev) => [...prev, novaMensagemAgente]);
    } catch (err) {
      setError(err.message || 'Erro ao processar sua pergunta.');
      console.error('Erro Chat API:', err);
    } finally {
      setIsLoading(false);
    }
  }, []);

  /**
   * Limpa o histórico de mensagens.
   */
  const limparHistorico = useCallback(() => {
    setMessages([]);
    setError('');
  }, []);

  return {
    messages,
    isLoading,
    error,
    enviarPergunta,
    limparHistorico,
  };
};

export default useChatAPI;
