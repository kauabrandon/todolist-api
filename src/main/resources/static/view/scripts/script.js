const tasksEndpoint = "http://localhost:8080/task/user";

function hideLoader() {
  document.getElementById("loading").style.display = "none";
}

function show(tasks) {
  let tab = `<thead>
            <th scope="col">#</th>
            <th scope="col">Descrição</th>
        </thead>`;

  for (let task of tasks) {
    tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td>${task.description}</td>
            </tr>
        `;
  }

  document.getElementById("tasks").innerHTML = tab;
}

async function getTasks() {
  const token = localStorage.getItem("Authorization");

  const response = await fetch(tasksEndpoint, {
    method: "GET",
    headers: new Headers({
        Authorization: token,
        }),
  });

  hideLoader();

  if (!response.ok) {
    localStorage.removeItem("Authorization");
    window.location = "/view/login.html";
    return;
    }

  const data = await response.json();
  show(data);
}

document.addEventListener("DOMContentLoaded", function () {
  if (!localStorage.getItem("Authorization")) {
    window.location = "/view/login.html"
    return;
  }
  getTasks();
});