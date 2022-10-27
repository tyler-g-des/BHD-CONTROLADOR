let element = document.getElementsByClassName("nav-right").item(0);

element.insertAdjacentHTML('afterbegin','<li class="m-r-10"><a href="#"><span id="nombreDocumento" class="badge badge-primary">projectName</span></a></li>');

const starttime=document.getElementsByClassName('nav-logo'); 

starttime[0].remove();

const fondo=document.getElementsByClassName('badge badge-primary');

fondo[1].removeAttribute('class');

let elementTypeRow = document.getElementsByClassName("container-fluid p-4 view dashboard-view");

elementTypeRow[0].setAttribute('id',"dashboardStadistic");

let blockTest = elementTypeRow[0].children[0];

blockTest.children.item(3).setAttribute('class','col-md-2');

blockTest.children.item(2).setAttribute('class','col-md-2');

blockTest.children.item(1).setAttribute('class','col-md-2');

blockTest.children.item(0).setAttribute('class','col-md-2');


let elements=document.getElementsByTagName('H3');

let elementoFecha1= elements[0].innerHTML;
let elementoFecha2= elements[1].innerHTML;

let startExecution= new Date(elementoFecha1);

let endExecution= new Date(elementoFecha2);

let result= new Date(endExecution-startExecution);

blockTest.insertAdjacentHTML('beforeend','<div class="col-md-2"> <div class="card"><div class="card-body"><p class="m-b-0">Execution Time</p><h3>'+result.getUTCHours().toString().padStart(2,0)+":"+result.getMinutes().toString().padStart(2,0)+":"+result.getUTCSeconds().toString().padStart(2,0)+'</h3></div></div></div>');