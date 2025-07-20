function loadListStudent(pageListStudent){
    const sizeListStudent = 10;
    const urlListStudent = `http://localhost:9999/api/identity/users/getUserRole/STUDENT?page=${pageListStudent}&size=${sizeListStudent}`;
    
    fetch(urlListStudent)
        .then(res => res.json())
        .then(data => {
            const studentList = data.result || [];
            console.log("student list: \n", studentList);

            const tableBody = document.getElementById("elementUser"); 
            tableBody.innerHTML = ""; // clear cũ

            studentList.data.forEach((student, index) => {
                const row = document.createElement("tr");
                row.className = `table-row hover:bg-gray-50 dark:hover:bg-gray-700`;
                row.innerHTML = `
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">${student.userId}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="flex-shrink-0 h-10 w-10">
                        <img src="${student.avatar}" alt="Portrait of a young male user with short hair and casual clothing" class="h-10 w-10 rounded-full">
                      </div>
                      <div class="ml-4">
                        <div class="text-sm font-medium text-gray-900 dark:text-white">${student.fullName}</div>
                        <div class="text-sm text-gray-500 dark:text-gray-400">@Chưa cập nhật</div>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">${new Date(student.dob).toLocaleDateString('vi-VN')}</td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">${student.gender}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200">${student.phoneNumber}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button data-user-id-promote="${student.userId}" data-fullName="${student.fullName}" class="text-indigo-600 hover:text-indigo-900 dark:text-indigo-400 dark:hover:text-indigo-300 mr-3">
                      <i class="fas fa-user-graduate"></i> Promote
                    </button>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button data-user-id="${student.userId}" class="text-indigo-600 hover:text-indigo-900 dark:text-indigo-400 dark:hover:text-indigo-300 mr-3">
                      <i class="fa-solid fa-trash"></i> Delete
                    </button>
                  </td>
                `;
                tableBody.appendChild(row);
            });
            updatePaginationControls(studentList.totalPage, pageListStudent);
        })
        .catch(err => console.error("Lỗi khi load danh sách student:", err));
}

function loadListTeacher(pageListTeacher){
    const sizeListTeacher = 10;
    const urlListStudent = `http://localhost:9999/api/identity/users/getUserRole/TEACHER?page=${pageListTeacher}&size=${sizeListTeacher}`;
    
    fetch(urlListStudent)
        .then(res => res.json())
        .then(data => {
            const studentTeacher = data.result || [];
            console.log("student teacher: \n", studentTeacher);

            const tableBody = document.getElementById("elementTeacher");
            tableBody.innerHTML = "";

            studentTeacher.data.forEach((teacher, index) => {
                const row = document.createElement("tr");
                row.className = `table-row hover:bg-gray-50 dark:hover:bg-gray-700`;
                row.innerHTML = `
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">${teacher.userId}</td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="flex-shrink-0 h-10 w-10">
                        <img src="${teacher.avatar}" alt="Portrait of a young male user with short hair and casual clothing" class="h-10 w-10 rounded-full">
                      </div>
                      <div class="ml-4">
                        <div class="text-sm font-medium text-gray-900 dark:text-white">${teacher.fullName}</div>
                        <div class="text-sm text-gray-500 dark:text-gray-400">@Chưa cập nhật</div>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300"> ${new Date(teacher.dob).toLocaleDateString('vi-VN')}</td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">${teacher.gender}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200">${teacher.phoneNumber}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button data-user-id="${teacher.userId}" class="text-indigo-600 hover:text-indigo-900 dark:text-indigo-400 dark:hover:text-indigo-300 mr-3">
                      <i class="fa-solid fa-trash"></i> Delete
                    </button>
                  </td>
                `;
                tableBody.appendChild(row);
            });
            updatePaginationControls1(studentTeacher.totalPage, pageListTeacher);
        })
        .catch(err => console.error("Lỗi khi load danh sách teacher:", err));
}

function deleteTeacherinAdmin (){
  const token = localStorage.getItem('tokenAdmin');
  const tableBody = document.getElementById("elementTeacher");
  tableBody.addEventListener("click", function (event) {
      const target = event.target.closest("button[data-user-id]");
      if (target) {
          const userId = target.getAttribute("data-user-id");
          console.log("Click Delete, userId:", userId);
          const urlDeleteTeacherInAdmin = `http://localhost:9999/api/identity/users/deleteUser/${userId}`;
          fetch(urlDeleteTeacherInAdmin, {
            method: "DELETE",
            headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
            }
          })
          .then((Response) => Response.json())
          .then((data) => {
            alert('delete user role teacher success');
            loadListTeacher(0);
          })
          .catch((err)=>{
            alert('error when call api delete user role teacher');
            console.log("error when call api delete user role teacher: ", err);
          })
      }
  });
}


function deleteStudentInAdmin (){
  const token = localStorage.getItem('tokenAdmin');
  const tableBody = document.getElementById("elementUser");
  tableBody.addEventListener("click", function (event) {
      const target = event.target.closest("button[data-user-id]");
      if (target) {
          const userId = target.getAttribute("data-user-id");
          console.log("Click Delete, userId:", userId);
          const urlDeleteTeacherInAdmin = `http://localhost:9999/api/identity/users/deleteUser/${userId}`;
          fetch(urlDeleteTeacherInAdmin, {
            method: "DELETE",
            headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
            }
          })
          .then((Response) => Response.json())
          .then((data) => {
            alert('delete user role student success');
            loadListStudent(0);
          })
          .catch((err)=>{
            alert('error when call api delete user role student');
            console.log("error when call api delete user role student: ", err);
          })
      }
  });
}

let promoteResolve = null;
let currentUserId = null;
let currentUserName = null;

// Hiển thị modal xác nhận
function showPromoteConfirm(userId, userName) {
  currentUserId = userId;
  currentUserName = userName;

  document.getElementById('modal-message').textContent =
    `Bạn có muốn nâng cấp tài khoản có tên là ${userName} (ID: ${userId}) thành tài khoản teacher không?`;

  document.getElementById('promote-modal').classList.remove('hidden');

  return new Promise((resolve) => {
    promoteResolve = resolve;
  });
}

// Ẩn modal (không quyết định true/false)
function hidePromoteModal() {
  document.getElementById('promote-modal').classList.add('hidden');
}

// Hủy nâng cấp
function cancelPromote() {
  hidePromoteModal();
  if (promoteResolve) {
    promoteResolve(false);
    promoteResolve = null;
  }
}

// Xác nhận nâng cấp
function promoteUser() {
  hidePromoteModal();
  if (promoteResolve) {
    promoteResolve(true);
    promoteResolve = null;
  }

  // Hiển thị toast
  document.getElementById('toast-title').textContent = 'Nâng cấp thành công';
  document.getElementById('toast-message').textContent =
    `${currentUserName} đã được nâng cấp lên thành teacher.`;
  document.getElementById('success-toast').classList.remove('hidden');

  setTimeout(hideToast, 3000);
}

// Ẩn toast
function hideToast() {
  document.getElementById('success-toast').classList.add('hidden');
}

// Gắn sự kiện click cho nút Promote
function setupPromoteListener() {
  const tokenAdmin = localStorage.getItem('tokenAdmin');
  const tableBody = document.getElementById("elementUser");

  tableBody.addEventListener("click", async function (event) {
    const target = event.target.closest("button[data-user-id-promote]");
    if (!target) return;

    const userId = target.getAttribute("data-user-id-promote");
    const fullName = target.getAttribute("data-fullName");

    const confirmed = await showPromoteConfirm(userId, fullName);
    if (!confirmed) return;

    const url = `http://localhost:9999/api/identity/users/updateUserForAdmin/${userId}`;
    const data = { roles: ["TEACHER"] };

    try {
      const response = await fetch(url, {
        method: "PUT",
        headers: {
          "Authorization": `Bearer ${tokenAdmin}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });

      if (!response.ok) {
        throw new Error("Lỗi server");
      }

      await response.json();
      promoteUser();
      loadListStudent(0);
    } catch (err) {
      alert('Lỗi khi cập nhật tài khoản từ Student sang Teacher.');
      console.error("Lỗi cập nhật:", err);
    }
  });
}



function updatePaginationControls(totalPages, current) {
    console.log("total page: ", totalPages);
    const pagination = document.getElementById("paginationControls");
    pagination.innerHTML = "";

    const prevBtn = document.createElement("button");
    prevBtn.textContent = "Previous";
    prevBtn.disabled = current === 0;
    prevBtn.className = "px-3 py-1 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50";
    prevBtn.onclick = () => {
        if (current > 0) {
            currentPage--;
            loadListStudent(currentPage);
        }
    };
    pagination.appendChild(prevBtn);

    for (let i = 0; i < totalPages; i++) {
        const pageBtn = document.createElement("button");
        pageBtn.textContent = i + 1;
        pageBtn.className = `px-3 py-1 border ${i === current ? "bg-indigo-100 border-indigo-300 text-indigo-600" : "border-gray-300 text-gray-700 hover:bg-gray-50"} rounded-md text-sm font-medium`;
        pageBtn.onclick = () => {
            currentPage = i;
            loadListStudent(currentPage);
        };
        pagination.appendChild(pageBtn);
    }

    const nextBtn = document.createElement("button");
    nextBtn.textContent = "Next";
    nextBtn.disabled = current >= totalPages - 1;
    nextBtn.className = "px-3 py-1 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50";
    nextBtn.onclick = () => {
        if (current < totalPages - 1) {
            currentPage++;
            loadListStudent(currentPage);
        }
    };
    pagination.appendChild(nextBtn);
}

function updatePaginationControls1(totalPages, current) {
    console.log("total page: ", totalPages);
    const pagination = document.getElementById("paginationControls1");
    pagination.innerHTML = "";

    const prevBtn = document.createElement("button");
    prevBtn.textContent = "Previous";
    prevBtn.disabled = current === 0;
    prevBtn.className = "px-3 py-1 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50";
    prevBtn.onclick = () => {
        if (current > 0) {
            currentPage--;
            loadListStudent(currentPage);
        }
    };
    pagination.appendChild(prevBtn);

    for (let i = 0; i < totalPages; i++) {
        const pageBtn = document.createElement("button");
        pageBtn.textContent = i + 1;
        pageBtn.className = `px-3 py-1 border ${i === current ? "bg-indigo-100 border-indigo-300 text-indigo-600" : "border-gray-300 text-gray-700 hover:bg-gray-50"} rounded-md text-sm font-medium`;
        pageBtn.onclick = () => {
            currentPage = i;
            loadListStudent(currentPage);
        };
        pagination.appendChild(pageBtn);
    }

    const nextBtn = document.createElement("button");
    nextBtn.textContent = "Next";
    nextBtn.disabled = current >= totalPages - 1;
    nextBtn.className = "px-3 py-1 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50";
    nextBtn.onclick = () => {
        if (current < totalPages - 1) {
            currentPage++;
            loadListStudent(currentPage);
        }
    };
    pagination.appendChild(nextBtn);
}

function logoutPageAdmin (){
  const urlLogoutPageAdmin = "http://localhost:9999/api/identity/auth/logout";
  const tokenAdmin = localStorage.getItem('tokenAdmin');
  const dataRequest = {
    "token": tokenAdmin
  }
  fetch(urlLogoutPageAdmin, {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${tokenAdmin}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify(dataRequest)
  })
  .then((response) => response.json())
  .then((data) => {
    localStorage.removeItem('tokenAdmin');
    window.location.href = "index.html";
  })
  .catch((err) => {
    console.log("error when logout admin: ", err);
    alert("error when logout admin");
  })
}

window.addEventListener("DOMContentLoaded", () => {
    console.log("token admin: " , localStorage.getItem('tokenAdmin'))
    const totalUserNumber = document.getElementById('totalUserNumber');
    const urlTotalUserWithStudent = "http://localhost:9999/api/identity/users/countUserWithRole/STUDENT";
    fetch(urlTotalUserWithStudent, {
      method: "GET",
      headers: {
      "Content-Type": "application/json"
      }
    })
    .then((response) => response.json())
    .then((data) => {
      totalUserNumber.innerText = data.result.countUser;
    })
    .catch((err)=> {
      alert("error urlTotalUserWithStudent ");
      console.log("error urlTotalUserWithStudent: \n", err)
    })
    const totalTeacherNumber = document.getElementById('totalTeacherNumber');
    const urlTotalUserWithTeacher = "http://localhost:9999/api/identity/users/countUserWithRole/TEACHER";
    fetch(urlTotalUserWithTeacher, {
      method: "GET",
      headers: {
      "Content-Type": "application/json"
      }
    })
    .then((response) => response.json())
    .then((data) => {
      totalTeacherNumber.innerText = data.result.countUser;
    })
    .catch((err)=> {
      alert("error urlTotalUserWithTeacher ");
      console.log("error urlTotalUserWithTeacher: \n", err)
    })
    
    const totalUser = document.getElementById("totalUser");
    const totalTeacher = document.getElementById("totalTeacher");
    const userTable = document.getElementById("userTable");
    const teacherTable = document.getElementById("teacherTable");
    totalUser.onclick = () => {
        teacherTable.classList.add('hidden');
        loadListStudent(0);
        userTable.classList.remove('hidden');
        deleteStudentInAdmin();
        setupPromoteListener();
    }
    totalTeacher.onclick = () => {
        userTable.classList.add('hidden');
        loadListTeacher(0);
        teacherTable.classList.remove('hidden');
        deleteTeacherinAdmin();
    }
    const logoutAdminBtn = document.getElementById("logoutAdmin");
      logoutAdminBtn.onclick = () => {
        console.log("1")
      logoutPageAdmin();
    }
})