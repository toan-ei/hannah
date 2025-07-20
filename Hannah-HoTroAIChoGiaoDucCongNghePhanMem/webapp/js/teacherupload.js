function sendData() {
  const mediaFile = document.getElementById('media').files[0];
  const titleOnly = document.getElementById('request').value.trim();
  const requestJSON = JSON.stringify({ title: titleOnly });

  const formData = new FormData();
  formData.append('media', mediaFile);
  formData.append('request', requestJSON);
  const token = localStorage.getItem('token');

  fetch('http://localhost:9999/api/post/createVideoPost', {
    method: 'POST',
    headers: {
      "Authorization": `Bearer ${token}`
    },
    body: formData
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Upload failed');
    }
    return response.json();
  })
  .then(data => {
    console.log('Success:', data);
    alert("upload video thành công");
  })
  .catch(error => {
    console.error('error when call api upload video for role teacher:', error);
    alert('error when call api upload video for role teacher');
  });
}

window.addEventListener("DOMContentLoaded", () => {
    const submitUploadFile = document.getElementById('submituploadfile');
    submitUploadFile.onclick = () => {
        sendData();
    }
})