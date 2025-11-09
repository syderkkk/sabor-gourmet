/**
 * Sabor Gourmet - JavaScript para gestión de pedidos
 */

// Inicialización cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    console.log('Sistema de Pedidos - Sabor Gourmet inicializado');

    // Auto-dismiss de alertas después de 5 segundos
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Confirmación para acciones destructivas
    const deleteButtons = document.querySelectorAll('[data-confirm]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm');
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    // Tooltip de Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});

/**
 * Calcula el subtotal de un detalle de pedido
 */
function calcularSubtotal(selectPlato, inputCantidad) {
    const precio = parseFloat(selectPlato.options[selectPlato.selectedIndex].dataset.precio) || 0;
    const cantidad = parseInt(inputCantidad.value) || 0;
    return precio * cantidad;
}

/**
 * Actualiza el total del pedido
 */
function actualizarTotal() {
    let total = 0;
    const detalles = document.querySelectorAll('.detalle-item');

    detalles.forEach(detalle => {
        const select = detalle.querySelector('select');
        const input = detalle.querySelector('input[type="number"]');
        if (select && input) {
            total += calcularSubtotal(select, input);
        }
    });

    const totalElement = document.getElementById('totalPedido');
    if (totalElement) {
        totalElement.textContent = 'S/ ' + total.toFixed(2);
    }
}

/**
 * Formatea números como moneda peruana
 */
function formatearMoneda(numero) {
    return new Intl.NumberFormat('es-PE', {
        style: 'currency',
        currency: 'PEN'
    }).format(numero);
}

/**
 * Valida el formulario de pedido antes de enviar
 */
function validarFormularioPedido(form) {
    const mesa = form.querySelector('#idMesa');
    const detalles = form.querySelectorAll('.detalle-item');

    if (!mesa || !mesa.value) {
        alert('Debe seleccionar una mesa');
        return false;
    }

    if (detalles.length === 0) {
        alert('Debe agregar al menos un plato al pedido');
        return false;
    }

    let valid = true;
    detalles.forEach(detalle => {
        const select = detalle.querySelector('select');
        const input = detalle.querySelector('input[type="number"]');

        if (!select.value || !input.value || input.value < 1) {
            valid = false;
        }
    });

    if (!valid) {
        alert('Todos los detalles deben tener un plato y cantidad válidos');
        return false;
    }

    return true;
}

/**
 * Filtrar tabla por texto
 */
function filtrarTabla(inputId, tableId) {
    const input = document.getElementById(inputId);
    const filter = input.value.toUpperCase();
    const table = document.getElementById(tableId);
    const tr = table.getElementsByTagName('tr');

    for (let i = 1; i < tr.length; i++) {
        let txtValue = tr[i].textContent || tr[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            tr[i].style.display = '';
        } else {
            tr[i].style.display = 'none';
        }
    }
}

/**
 * Actualización automática de la vista de cocina
 */
function configurarActualizacionAutomatica(intervalo = 30000) {
    const path = window.location.pathname;

    if (path.includes('/cocina')) {
        setInterval(() => {
            console.log('Actualizando vista de cocina...');
            location.reload();
        }, intervalo);
    }
}

// Ejecutar configuración de actualización automática
configurarActualizacionAutomatica();

/**
 * Imprime el detalle del pedido
 */
function imprimirPedido() {
    window.print();
}

/**
 * Exporta datos a CSV (básico)
 */
function exportarACSV(tableId, filename = 'datos.csv') {
    const table = document.getElementById(tableId);
    let csv = [];
    const rows = table.querySelectorAll('tr');

    for (let i = 0; i < rows.length; i++) {
        let row = [], cols = rows[i].querySelectorAll('td, th');

        for (let j = 0; j < cols.length; j++) {
            row.push(cols[j].innerText);
        }

        csv.push(row.join(','));
    }

    const csvFile = new Blob([csv.join('\n')], { type: 'text/csv' });
    const downloadLink = document.createElement('a');
    downloadLink.download = filename;
    downloadLink.href = window.URL.createObjectURL(csvFile);
    downloadLink.style.display = 'none';
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
}
