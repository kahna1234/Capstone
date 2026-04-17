// ============================================
// MODAL COMPONENT - ES6 Module
// ============================================

export class Modal {
    static open(title, content, onConfirm = null, onCancel = null) {
        const container = document.getElementById('modal-container');
        const modal = document.createElement('div');
        modal.className = 'modal-overlay active';

        modal.innerHTML = `
            <div class="modal">
                <div class="modal-header">
                    <h2>${title}</h2>
                    <button class="modal-close">&times;</button>
                </div>
                <div class="modal-body">
                    ${content}
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary modal-cancel">Cancel</button>
                    <button class="btn btn-primary modal-confirm">Confirm</button>
                </div>
            </div>
        `;

        container.innerHTML = '';
        container.appendChild(modal);

        const closeBtn = modal.querySelector('.modal-close');
        const cancelBtn = modal.querySelector('.modal-cancel');
        const confirmBtn = modal.querySelector('.modal-confirm');

        const close = () => {
            modal.classList.remove('active');
            setTimeout(() => modal.remove(), 300);
        };

        closeBtn.addEventListener('click', close);
        cancelBtn.addEventListener('click', () => {
            close();
            if (onCancel) onCancel();
        });

        confirmBtn.addEventListener('click', () => {
            close();
            if (onConfirm) onConfirm();
        });

        modal.addEventListener('click', (e) => {
            if (e.target === modal) close();
        });
    }

    static alert(title, message) {
        this.open(title, `<p>${message}</p>`);
    }

    static confirm(title, message, onConfirm, onCancel) {
        this.open(title, `<p>${message}</p>`, onConfirm, onCancel);
    }
}
