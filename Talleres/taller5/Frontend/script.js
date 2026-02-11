const API_URL = "http://3.90.221.224:8080/properties";

document.addEventListener("DOMContentLoaded", () => {
    loadProperties();
});

document.getElementById("property-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("property-id").value;
    const property = {
        address: document.getElementById("address").value,
        price: parseFloat(document.getElementById("price").value),
        size: parseInt(document.getElementById("size").value),
        descripction: document.getElementById("description").value
    };

    try {
        if (id) {
            await fetch(`${API_URL}/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(property)
            });
        } else {
            await fetch(API_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(property)
            });
        }

        document.getElementById("property-form").reset();
        document.getElementById("property-id").value = "";
        loadProperties();
    } catch (err) {
        console.error(err);
    }
});

// Cargar todas las propiedades
async function loadProperties() {
    try {
        const response = await fetch(API_URL);
        const properties = await response.json();

        const tbody = document.querySelector("#properties-table tbody");
        tbody.innerHTML = "";

        properties.forEach(p => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${p.id}</td>
                <td>${p.address}</td>
                <td>${p.price}</td>
                <td>${p.size}</td>
                <td>${p.descripction}</td>
                <td>
                    <button class="action-button edit" onclick="editProperty(${p.id})">Editar</button>
                    <button class="action-button" onclick="deleteProperty(${p.id})">Eliminar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error(err);
    }
}

// Editar propiedad
async function editProperty(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const property = await response.json();

        document.getElementById("property-id").value = property.id;
        document.getElementById("address").value = property.address;
        document.getElementById("price").value = property.price;
        document.getElementById("size").value = property.size;
        document.getElementById("description").value = property.descripction;

    } catch (err) {
        console.error(err);
    }
}

// Eliminar propiedad
async function deleteProperty(id) {
    if (!confirm("¿Seguro que quieres eliminar esta propiedad?")) return;
    try {
        await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        loadProperties();
    } catch (err) {
        console.error(err);
    }
}
