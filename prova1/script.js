// ==============================================================
// 		EVENTOS

// RESET
reset = function() {
	// Aqui você cria uma requisição AJAX POST a ControllerServlet
	// Você repassa, com a chave 'op' o parâmetro 'RESET'
	// Se a requisição for bem sucedida, você executa:
	// atualizaSessao() e window.location.href = "/prova1".
	// Se não for bem sucedida, decida o que fazer.
	const ajax = new XMLHttpRequest();
    ajax.open("POST", "ControllerServlet", true);
    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    ajax.onreadystatechange = () => {
		if((ajax.readyState == 4 || ajax.readyState == 2) && ajax.status == 200){
			atualizaSessao();
			window.location.href = "/prova1";
		}else {
			alert("Error on register informations")
		}
	}
	
	ajax.send(`op=RESET`);
}

// NOVA AULA
novaAula = function() {
	window.location.href = "nova";
}

// CANCELA NOVA AULA (OU EDIÇÃO)
calcelarNovaAula = function() {
	window.location.href = "/prova1";
}

// EDITA UMA AULA COM ID ESPECIFICADO
editarAula = function(id) {
	window.location.href = "edit?id=" + id;
}

// ENVIA CONTEÚDO DA NOVA AULA
enviarNovaAula = function() {
	// obtém os valores a partir do formulário
	let data = document.getElementById('data-id').value;
	let horario = document.getElementById('hora-id').value;
	let duracao = document.getElementById('dur-id').value;
	let codDisciplina = document.getElementById('disc-id').value;
	let assunto = document.getElementById('ass-id').value;
	// verificando a validação
	if (!validaNovaAula(data, horario, duracao, codDisciplina, assunto)) {
        document.getElementById('msg-id').style.display = 'block';
        return;
    }
    // Aqui, você faz uma requisição AJAX POST a ControllerServlet e
    // envia a chave 'op' valendo 'CREATE'. Envie, do mesmo modo, os parâmetros
    // data, horario, duracao, codDisciplina e assunto.
    // Se a requisição for bem sucedida, execute atualizaSessao() e
    // window.location.href = "/prova1"
    // Se não for bem sucedida, decida o que fazer
    console.log("teste")
    const ajax = new XMLHttpRequest();
    ajax.open("POST", "ControllerServlet", true);
    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    ajax.onreadystatechange = () => {
		if((ajax.readyState == 4 || ajax.readyState == 2) && ajax.status == 200){
			atualizaSessao();
			console.log("test teste")
			window.location.href = "/prova1";
		}else {
			alert("Error on register informations")
		}
	}
	
	ajax.send(`op=CREATE&horario=${horario}&duracao=${duracao}&codDisciplina=${codDisciplina}&assunto=${assunto}&data=${data}`);
}

// ENVIA CONTEÚDO EM EDIÇÃO
enviarEdit = function() {
	// obtém os valores a partir do formulário
	let id = document.getElementById('id').value;
	let data = document.getElementById('data-id').value;
	let horario = document.getElementById('hora-id').value;
	let duracao = document.getElementById('dur-id').value;
	let codDisciplina = document.getElementById('disc-id').value;
	let assunto = document.getElementById('ass-id').value;
	// Aqui, você faz uma requisição AJAX POST a ControllerServlet e
    // envia a chave 'op' valendo 'UPDATE'. Envie, do mesmo modo, os parâmetros
    // id, data, horario, duracao, codDisciplina e assunto.
    // Se a requisição for bem sucedida, execute atualizaSessao() e
    // window.location.href = "/prova1"
    // Se não for bem sucedida, decida o que fazer
    const ajax = new XMLHttpRequest();
    ajax.open("POST", "ControllerServlet", true);
    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    ajax.onreadystatechange = () => {
		if((ajax.readyState == 4 || ajax.readyState == 2) && ajax.status == 200){
			atualizaSessao();
			window.location.href = "/prova1";
		}else {
			alert("Error on edit register");
		}
	}
	ajax.send(`op=UPDATE&id=${id}&horario=${horario}&duracao=${duracao}&codDisciplina=${codDisciplina}&assunto=${assunto}&data=${data}`);
}

// DELETA UMA AULA
deleta = function(id) {
	// Aqui, você faz uma requisição AJAX POST a ControllerServlet e
    // envia a chave 'op' valendo 'DELETE'. Envie, do mesmo modo, o parâmetro id
    // Se a requisição for bem sucedida, execute atualizaSessao() e
    // window.location.href = "/prova1"
    // Se não for bem sucedida, decida o que fazer
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		if ((req.readyState == 4 || req.readyState == 2) && req.status == 200) {
			atualizaSessao();
			window.location.href = "/prova1";
		} else {
			alert("Error on delete register")
		}
	}
	req.send(`op=DELETE&id=${id}`);
}




const atualizaSessao = function() {
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		console.log(req.readyState, req.status)
		if ((req.readyState == 4 || req.readyState == 2) && req.status == 200) {
		} else {
			alert("Error on delete register")
		}
	}
	req.send("op=START_SESSION");
}



// ============================================================
// 			VALIDAÇÕES

validaNovaAula = function(data, horario, duracao, codDisciplina, assunto) {
    if (!data || !horario || !duracao || !codDisciplina || !assunto) {
	document.getElementById('msg-id').style.display = 'block';
        return false;
    }
    if (!/^\d{2}:\d{2}$/.test(horario)) {
	document.getElementById('msg-id').style.display = 'block';
        return false;
    }
    
    if (!/^\d{4}-\d{2}-\d{2}$/.test(data)) {
	document.getElementById('msg-id').style.display = 'block';
        return false;
    }

    if (isNaN(parseFloat(duracao)) || parseFloat(duracao) <= 0) {
	document.getElementById('msg-id').style.display = 'block';
        return false;
    }
    return true;
}





// ===================================================================================
// 		INICIALIZA O PROCESSAMENTO

atualizaSessao();
