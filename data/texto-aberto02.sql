create view cotistas as
select distinct semestre, count(aluno.nome) as n_cotistas from matricula
inner join aluno on (ALUNO.CHAVE_ALUNO = MATRICULA.CHAVE_ALUNO_FK)
where aluno.ENTRADA = 'cota'
GROUP BY matricula.semestre;
