window.onload = () => {
    let eventSource = new EventSource("http://localhost:8080/alarm/subscribe");

    eventSource.addEventListener('open', event => {
        console.log("connection opened")
    })

    eventSource.addEventListener('connect', event => {
        console.log(event.data);
    })

    eventSource.addEventListener('alarm', event => {
        let message = event.data;
        console.log(message)

        if (message === 'new alarm') {
            console.log(message)
            $('#alarm-container').load(location.href + ' #alarm-container')
        }
    })

    eventSource.addEventListener('error', event => {
        console.log(event.target.readyState)
        if (event.target.readyState == EventSource.CLOSED) {
            console.log('eventsource closed (' + event.target.readyState + ')')
        }
        eventSource.close();
    })
}

function forwardToPost(alarmId, postId) {
    $.ajax({
        type: 'GET',
        url: '/alarm/check/' + alarmId,
        success: function (data) {
            console.log(data)
            location.href = "/posts/" + postId;
        },
        error: function (error) {
            console.log(error);
        }
    });
}