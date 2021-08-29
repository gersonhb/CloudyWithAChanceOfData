//Modal info
const btnInfo = document.getElementsByClassName('info');
const link = document.getElementsByClassName('link');

for (let i = 0; i < btnInfo.length; i++) {
    btnInfo[i].addEventListener('click', function () {
        const modalTablaResultado = document.getElementById('tablaResultado');

        fetch(link[i].getAttribute('value'))
            .then(response => response.text())
            .then(data => modalTablaResultado.innerHTML = data);
    })
}

//Configuracion Global

function ReemplazarContenido(contenido) {
    document.open();
    document.write(contenido);
    document.close();
  }

function ValidarRuta()
{
    let valorRuta = document.getElementById('valorRuta').value;
    let alerta = document.getElementById('validacionRuta');

    fetch("/validacion/validarRuta",{
        method: "POST",
        body: valorRuta
    })
        .then(response => response.json())
        .then(response => {
            alerta.innerHTML=response[0];
            if(response[1]=="1")
                alerta.classList.add("text-success");
            else
                alerta.classList.add("text-danger");
            });  
}

function ValidarCifrado(idBtn,idAlerta)
{
    let valorRuta = document.getElementById(idBtn).value;
    let alerta = document.getElementById(idAlerta);

    fetch("/validacion/validarCifrado",{
        method: "POST",
        body: valorRuta
    })
        .then(response => response.json())
        .then(response => {
            alerta.innerHTML=response[0];
            if(response[1]=="1")
                alerta.classList.add("text-success");
            else
                alerta.classList.add("text-danger");
            });  
}