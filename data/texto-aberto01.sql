--1.1
create view cotistas as
select distinct semestre, count(aluno.nome) as n_cotistas from matricula
inner join aluno on (ALUNO.CHAVE_ALUNO = MATRICULA.CHAVE_ALUNO_FK)
where aluno.ENTRADA = 'cota'
GROUP BY matricula.semestre;

create view reprovados_cota as
select distinct semestre, count(aluno.nome) as reprovados from matricula
inner join aluno on (ALUNO.CHAVE_ALUNO = MATRICULA.CHAVE_ALUNO_FK)
where aluno.ENTRADA = 'cota' and matricula.status = 'Reprovado'
GROUP BY matricula.semestre;

select * from cotistas;
select * from reprovados_cota;

select cotistas.semestre, reprovados_cota.reprovados/cotistas.n_cotistas as desempenho_cotas from cotistas
inner join reprovados_cota on (cotistas.SEMESTRE = reprovados_cota.SEMESTRE)

-- 1.2
create view matriculados_curso as
select distinct curso.nome as curso, semestre, count(aluno.nome) as n_matriculados from matricula
inner join aluno on (ALUNO.CHAVE_ALUNO = MATRICULA.CHAVE_ALUNO_FK)
inner join curso on (curso.CHAVE_curso = aluno.CHAVE_curso_FK)
GROUP BY curso.nome, matricula.semestre;

create view reprovados_curso as
select distinct curso.nome as curso, semestre, count(aluno.nome) as reprovados from matricula
inner join aluno on (ALUNO.CHAVE_ALUNO = MATRICULA.CHAVE_ALUNO_FK)
inner join curso on (curso.CHAVE_curso = aluno.CHAVE_curso_FK)
where matricula.status = 'Reprovado'
GROUP BY curso.nome, matricula.semestre;

select * from matriculados_curso;
select * from reprovados_curso;

select distinct matriculados_curso.curso, matriculados_curso.semestre, reprovados_curso.reprovados/matriculados_curso.n_matriculados as desempenho_curso 
from matriculados_curso
inner join reprovados_curso on (matriculados_curso.curso = reprovados_curso.curso and matriculados_curso.semestre = reprovados_curso.semestre);

-- 1.3

create view matriculados_disciplina as
select distinct disciplina.nome as disciplina, matricula.semestre, count(aluno.nome) as n_matriculados from disciplina
inner join matricula on (disciplina.CHAVE_disciplina = matricula.chave_disciplina_fk)
inner join aluno on (ALUNO.CHAVE_ALUNO = MATRICULA.CHAVE_ALUNO_FK)
GROUP BY disciplina.nome, matricula.semestre;

create view reprovados_disciplina as
select distinct disciplina.nome as disciplina, matricula.semestre, count(aluno.nome) as reprovados from disciplina
inner join matricula on (disciplina.CHAVE_disciplina = matricula.chave_disciplina_fk)
inner join aluno on (ALUNO.CHAVE_ALUNO = MATRICULA.CHAVE_ALUNO_FK)
where matricula.status = 'Reprovado'
GROUP BY disciplina.nome, matricula.semestre;

select * from matriculados_disciplina;
select * from reprovados_disciplina;

select distinct matriculados_disciplina.disciplina, matriculados_disciplina.semestre, reprovados_disciplina.reprovados/matriculados_disciplina.n_matriculados as desempenho_curso 
from matriculados_disciplina
inner join reprovados_disciplina on (matriculados_disciplina.disciplina = reprovados_disciplina.disciplina and matriculados_disciplina.semestre = reprovados_disciplina.semestre);
