-- A senha passou a ser opcional (quem foi cadastrado sem senha a define no primeiro acesso).
-- Bancos criados antes dessa mudança ainda têm a coluna como NOT NULL.
ALTER TABLE tb_usuarios MODIFY senha VARCHAR(255) NULL;

-- O ddl-auto=update não muda o tipo de colunas existentes. Sem LONGBLOB,
-- o arquivo do certificado ficava em varbinary(255) e qualquer PDF real era recusado.
ALTER TABLE tb_certificacoes MODIFY dados_arquivo LONGBLOB NULL;
