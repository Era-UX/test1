let modalInstance = null;
// Глобальные переменные
let allStudents = [];
let allTeachers = [];

async function loadDashboardData() {
    try {
        const response = await fetch('/api/data');
        const data = await response.json();

        // 1. Сохраняем студентов
        allStudents = data.students;
        // 2. Раздаем им "Красивые ID" по порядку (1, 2, 3...)
        // Эти номера "прилипнут" к студентам
        allStudents.forEach((s, index) => {
            s.fakeId = index + 1;
        });

        // То же самое для учителей
        allTeachers = data.teachers;
        allTeachers.forEach((t, index) => {
            t.fakeId = index + 1;
        });

        // Обновляем статистику (остальной код тот же)
        document.getElementById('uni-name').innerText = data.institution.name;
        document.getElementById('uni-address').innerText = data.institution.address;
        document.getElementById('stat-students').innerText = data.institution.totalStudents;
        document.getElementById('stat-teachers').innerText = data.institution.totalTeachers;

        renderStudents(allStudents);
        renderTeachers(allTeachers);

    } catch (error) {
        console.error("Error:", error);
    }
}

// 2. ФУНКЦИЯ ПОИСКА (Фильтрация)
function handleSearch() {
    const query = document.getElementById('searchInput').value.toLowerCase();

    // Фильтруем студентов (по имени или ID)
    const filteredStudents = allStudents.filter(s =>
        s.name.toLowerCase().includes(query) ||
        s.id.toString().includes(query)
    );

    // Фильтруем учителей (по имени, предмету или ID)
    const filteredTeachers = allTeachers.filter(t =>
        t.name.toLowerCase().includes(query) ||
        t.subject.toLowerCase().includes(query) ||
        t.id.toString().includes(query)
    );

    // Перерисовываем таблицы с отфильтрованными данными
    renderStudents(filteredStudents);
    renderTeachers(filteredTeachers);
}

function renderStudents(students) {
    const table = document.getElementById('students-table');
    table.innerHTML = '';

    students.forEach((s) => {
        table.innerHTML += `
            <tr>
                <td data-label="ID"><b>${s.fakeId}</b></td>
                
                <td data-label="Name">${s.name}</td>
                <td data-label="Age">${s.age}</td>
                <td data-label="Curator" class="text-primary">${s.teacherName || 'None'}</td>

                <td data-label="Actions">
                    <button class="btn btn-sm btn-outline-primary" 
                        onclick="openEditModal('student', ${s.id}, '${s.name}', ${s.age})">Edit</button>
                    <button class="btn btn-sm btn-outline-danger" 
                        onclick="deleteEntity('student', ${s.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

function renderTeachers(teachers) {
    const table = document.getElementById('teachers-table');
    table.innerHTML = '';

    teachers.forEach((t) => {
        table.innerHTML += `
            <tr>
                <td data-label="ID"><b>${t.fakeId}</b></td>
                
                <td data-label="Name">${t.name}</td>
                <td data-label="Subject">${t.subject}</td>
                <td data-label="Exp">${t.experience}y</td>
                <td data-label="Actions">
                    <button class="btn btn-sm btn-outline-primary" 
                        onclick="openEditModal('teacher', ${t.id}, '${t.name}', null, '${t.subject}', ${t.experience})">Edit</button>
                    <button class="btn btn-sm btn-outline-danger" 
                        onclick="deleteEntity('teacher', ${t.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

// 3. ОТКРЫТИЕ МОДАЛКИ (Для Создания или Редактирования)
function openEditModal(type, id = null, name = '', age = 18, subject = '', exp = 1) {
    // Заполняем скрытое поле ID. Если ID есть - это редактирование.
    document.getElementById('editId').value = id ? id : '';

    // Заполняем поля формы
    document.getElementById('inputType').value = type;
    document.getElementById('inputName').value = name;
    document.getElementById('inputAge').value = age;
    document.getElementById('inputSubject').value = subject;
    document.getElementById('inputExp').value = exp;

    // Блокируем смену типа и имя при редактировании (так как в DAO обновляем только возраст/опыт)
    document.getElementById('inputType').disabled = !!id;
    document.getElementById('inputName').disabled = !!id;

    // Показываем нужные поля
    toggleFields();

    // Открываем модалку через Bootstrap
    const modalEl = document.getElementById('addModal');
    modalInstance = new bootstrap.Modal(modalEl);
    modalInstance.show();
}

// 4. УПРАВЛЕНИЕ ПОЛЯМИ
function toggleFields() {
    const type = document.getElementById('inputType').value;
    if (type === 'student') {
        document.getElementById('field-age').style.display = 'block';
        document.getElementById('teacher-fields').style.display = 'none';
    } else {
        document.getElementById('field-age').style.display = 'none';
        document.getElementById('teacher-fields').style.display = 'block';
    }
}

// 5. ОТПРАВКА ДАННЫХ (CREATE или UPDATE)
async function submitCreate() {
    const id = document.getElementById('editId').value; // Проверяем, есть ли ID
    const type = document.getElementById('inputType').value;

    const payload = {
        type: type,
        name: document.getElementById('inputName').value,
        age: parseInt(document.getElementById('inputAge').value) || 0,
        subject: document.getElementById('inputSubject').value,
        experience: parseInt(document.getElementById('inputExp').value) || 0
    };

    let url = '/api/create';
    let method = 'POST';

    // Если ID существует, значит мы ОБНОВЛЯЕМ (UPDATE)
    if (id) {
        url = '/api/update';
        method = 'PUT';
        payload.id = parseInt(id); // Добавляем ID в запрос
    }

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            modalInstance.hide(); // Закрываем окно
            loadDashboardData();  // Обновляем таблицу

            // Чистим форму для следующего раза
            document.getElementById('addForm').reset();
            document.getElementById('editId').value = '';
        } else {
            alert("Error saving data!");
        }
    } catch (error) {
        console.error(error);
        alert("Server error!");
    }
}

// 6. УДАЛЕНИЕ
async function deleteEntity(type, id) {
    if (!confirm(`Delete ${type} #${id}?`)) return;
    await fetch(`/api/delete?type=${type}&id=${id}`, { method: 'DELETE' });
    loadDashboardData();
}

// Привязываем открытие пустой модалки к кнопке "+ Add Person"
// Нам нужно найти кнопку в HTML и добавить ей onclick="openEditModal('student')"
// Или просто добавить слушатель здесь:
document.addEventListener('DOMContentLoaded', () => {
    loadDashboardData();
    // Находим кнопку Add и вешаем обработчик для "чистого" создания
    const addBtn = document.querySelector('.btn-success');
    addBtn.onclick = () => openEditModal('student');
});

// Переменные для направления сортировки (1 - по возрастанию, -1 - по убыванию)
let dirStudent = 1;
let dirTeacher = 1;

// СОРТИРОВКА СТУДЕНТОВ
function sortStudents(field) {
    dirStudent *= -1;

    // Обновляем стрелочки в заголовке студентов
    const headers = document.querySelectorAll('#students-header th');
    headers.forEach(th => {
        // Убираем старые стрелки, оставляем чистый текст
        th.innerHTML = th.innerHTML.replace(' ↑', '').replace(' ↓', '').replace(' ↕', '');
        // Добавляем нейтральную ↕ всем, кроме текущего поля
    });

    // Добавляем активную стрелку текущему полю
    const currentTh = event.currentTarget;
    currentTh.innerHTML += dirStudent === 1 ? ' ↑' : ' ↓';

    allStudents.sort((a, b) => {
        let valA = a[field];
        let valB = b[field];
        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        if (valA < valB) return -1 * dirStudent;
        if (valA > valB) return 1 * dirStudent;
        return 0;
    });

    handleSearch();
}

// СОРТИРОВКА УЧИТЕЛЕЙ
function sortTeachers(field) {
    dirTeacher *= -1;

    // Обновляем стрелочки в заголовке учителей
    const headers = document.querySelectorAll('#teachers-header th');
    headers.forEach(th => {
        th.innerHTML = th.innerHTML.replace(' ↑', '').replace(' ↓', '').replace(' ↕', '');
    });

    const currentTh = event.currentTarget;
    currentTh.innerHTML += dirTeacher === 1 ? ' ↑' : ' ↓';

    allTeachers.sort((a, b) => {
        let valA = a[field];
        let valB = b[field];
        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        if (valA < valB) return -1 * dirTeacher;
        if (valA > valB) return 1 * dirTeacher;
        return 0;
    });

    handleSearch();
}