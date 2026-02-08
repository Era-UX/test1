let modalInstance = null;
let allStudents = [];
let allTeachers = [];

async function loadDashboardData() {
    try {
        const response = await fetch('/api/data');
        const data = await response.json();

        allStudents = data.students;
        allStudents.forEach((s, index) => {
            s.fakeId = index + 1;
        });

        allTeachers = data.teachers;
        allTeachers.forEach((t, index) => {
            t.fakeId = index + 1;
        });

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
//#3. DataPool
function handleSearch() {
    const query = document.getElementById('searchInput').value.toLowerCase();

    const filteredStudents = allStudents.filter(s =>
        s.name.toLowerCase().includes(query) ||
        s.id.toString().includes(query)
    );

    const filteredTeachers = allTeachers.filter(t =>
        t.name.toLowerCase().includes(query) ||
        t.subject.toLowerCase().includes(query) ||
        t.id.toString().includes(query)
    );

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

function openEditModal(type, id = null, name = '', age = 18, subject = '', exp = 1) {
    document.getElementById('editId').value = id ? id : '';

    document.getElementById('inputType').value = type;
    document.getElementById('inputName').value = name;
    document.getElementById('inputAge').value = age;
    document.getElementById('inputSubject').value = subject;
    document.getElementById('inputExp').value = exp;

    document.getElementById('inputType').disabled = !!id;
    document.getElementById('inputName').disabled = !!id;

    toggleFields();

    const modalEl = document.getElementById('addModal');
    modalInstance = new bootstrap.Modal(modalEl);
    modalInstance.show();
}

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

async function submitCreate() {
    const id = document.getElementById('editId').value;
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

    if (id) {
        url = '/api/update';
        method = 'PUT';
        payload.id = parseInt(id);
    }

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            modalInstance.hide();
            loadDashboardData();

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

document.addEventListener('DOMContentLoaded', () => {
    loadDashboardData();
    const addBtn = document.querySelector('.btn-success');
    addBtn.onclick = () => openEditModal('student');
});

let dirStudent = 1;
let dirTeacher = 1;

function sortStudents(field) {
    dirStudent *= -1;

    const headers = document.querySelectorAll('#students-header th');
    headers.forEach(th => {
        th.innerHTML = th.innerHTML.replace(' ↑', '').replace(' ↓', '').replace(' ↕', '');
    });

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

function sortTeachers(field) {
    dirTeacher *= -1;

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