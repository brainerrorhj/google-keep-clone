const apiUrl = '/api/notes';

document.addEventListener('DOMContentLoaded', loadNotes);

function loadNotes() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(notes => {
            const notesList = document.getElementById('notes-list');
            notesList.innerHTML = ''; // Xóa danh sách ghi chú hiện tại

            notes.forEach(note => {
                const noteElement = document.createElement('div');
                noteElement.classList.add('note');
                noteElement.setAttribute('data-id', note.id);
                noteElement.innerHTML = `
                    <h3>${note.title}</h3>
                    <p class="note-content">${note.content}</p>
                    ${note.image ? `
                        <div class="image-preview">
                            <img src="http://localhost:8080/uploads/${note.image}" alt="Hình ảnh ghi chú" class="note-image">
                        </div>` : ''}
                    <button onclick="deleteNote(${note.id})">Xóa</button>
                `;

                // Thêm sự kiện click để thu gọn và mở rộng ghi chú
                noteElement.querySelector('.note-content').addEventListener('click', (e) => {
                    e.stopPropagation(); // Ngăn sự kiện lan rộng

                    // Đảm bảo chỉ một ghi chú được mở rộng tại một thời điểm
                    document.querySelectorAll('.note-content').forEach(content => {
                        if (content !== e.target) {
                            content.classList.remove('expanded');
                        }
                    });

                    const content = e.target;
                    content.classList.toggle('expanded');
                });

                notesList.insertBefore(noteElement, notesList.firstChild);
            });
        })
        .catch(error => console.error('Error loading notes:', error));
}

// Các hàm khác không thay đổi

function showNoteForm() {
    document.getElementById('note-form').style.display = 'block';
    document.getElementById('note-title').value = ''; // Xóa nội dung tiêu đề
    document.getElementById('note-content').value = ''; // Xóa nội dung
    document.getElementById('note-image').value = ''; // Xóa hình ảnh đã chọn
}

function hideNoteForm() {
    document.getElementById('note-form').style.display = 'none';
}

function saveNote() {
    const title = document.getElementById('note-title').value;
    const content = document.getElementById('note-content').value;
    const imageInput = document.getElementById('note-image');
    let image = null;

    // Kiểm tra độ dài nội dung
    if (content.length > 15000) { // Giới hạn nội dung không quá 15000 ký tự
        alert('Nội dung ghi chú quá dài. Vui lòng rút ngắn lại!');
        return;
    }

    // Tạo FormData để gửi dữ liệu bao gồm tệp hình ảnh (nếu có)
    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);

    if (imageInput.files.length > 0) {
        formData.append('image', imageInput.files[0]); // Thêm tệp hình ảnh nếu có
    }

    fetch(apiUrl, {
        method: 'POST',
        body: formData // Sử dụng FormData thay vì JSON
    })
    .then(response => {
        if (!response.ok) {
            // Nếu không phải mã trạng thái thành công (2xx), ném lỗi với thông báo từ response
            return response.text().then(errorMessage => {
                throw new Error('Server error: ' + errorMessage); // Đọc thông báo lỗi từ response
            });
        }
        return response.json(); // Nếu thành công, trả về dữ liệu JSON
    })
    .then(() => {
        loadNotes(); // Tải lại danh sách ghi chú sau khi lưu thành công
        hideNoteForm(); // Ẩn form ghi chú
    })
    .catch(error => {
        console.error('Error saving note:', error);
        alert('Đã xảy ra lỗi khi lưu ghi chú, vui lòng thử lại!'); // Hiển thị thông báo lỗi
    });
}

// Hàm xóa ghi chú
function deleteNote(noteId) {
    // Xác nhận trước khi xóa ghi chú
    if (confirm("Bạn có chắc chắn muốn xóa ghi chú này?")) {
        fetch(`${apiUrl}/${noteId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Ghi chú đã được xóa.');
                loadNotes(); // Tải lại danh sách ghi chú sau khi xóa thành công
            } else {
                alert('Không thể xóa ghi chú, vui lòng thử lại!');
            }
        })
        .catch(error => {
            console.error('Error deleting note:', error);
            alert('Đã xảy ra lỗi khi xóa ghi chú!');
        });
    }
}

// Xử lý sự kiện dán hình ảnh từ clipboard
function handlePaste(event) {
    const items = event.clipboardData.items;
    for (let i = 0; i < items.length; i++) {
        const item = items[i];
        if (item.type.indexOf("image") === 0) { // Kiểm tra xem có phải hình ảnh không
            const file = item.getAsFile();
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result; // Đặt nguồn ảnh từ dữ liệu dán
                img.classList.add('pasted-image');
                document.getElementById('note-content').appendChild(img);
            };
            reader.readAsDataURL(file);
        }
    }
}

// Thêm sự kiện dán vào trường nội dung
document.getElementById('note-content').addEventListener('paste', handlePaste);
