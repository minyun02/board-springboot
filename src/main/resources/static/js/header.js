let username = document.getElementById('username');

if (username != null) {
    $.ajax({
        type: 'GET',
        url: '/alarm/unchecked',
        success: function (data) {
            let notification = document.getElementById('notification-count');
            notification.textContent = data;

            if (data !== 0) {
                notification.style.display = 'block';
            }
        },
        error: function (error) {
            console.log(error);
        }
    });
}

