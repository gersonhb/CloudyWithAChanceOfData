const btnInfo = document.getElementsByClassName('info');
const link = document.getElementsByClassName('link');

for (let i = 0; i < btnInfo.length; i++) {
    btnInfo[i].addEventListener('click', function () {
        const modalTablaResultado = document.getElementById('tablaResultado');

        fetch(link[i].getAttribute('value'))
            .then(response => response.text())
            .then(data => modalTablaResultado.innerHTML = data);
        console.log(link[i].getAttribute('value'));
    })
}