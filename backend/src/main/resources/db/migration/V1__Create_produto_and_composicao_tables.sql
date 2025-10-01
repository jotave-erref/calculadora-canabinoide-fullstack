-- Tabela principal para armazenar os produtos (óleos)
CREATE TABLE produto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    volume_frasco_ml INT NOT NULL,
    gotas_por_ml INT NOT NULL,
    PRIMARY KEY (id)
);

-- Tabela para armazenar a composição de cada canabinoide dentro de um produto
CREATE TABLE composicao_canabinoide (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome_canabinoide VARCHAR(50) NOT NULL,
    concentracao_mg_por_ml DOUBLE PRECISION NOT NULL,
    produto_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_composicao_produto
        FOREIGN KEY (produto_id) REFERENCES produto(id)
        ON DELETE CASCADE
);