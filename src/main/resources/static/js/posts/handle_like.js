let totalLikes = 0;
function setLikes(data) {
    totalLikes = data.totalLikes;
    document.getElementById('total-likes').textContent = totalLikes + ' LIKES';

    if (data.hasLikedBefore) {
        document.getElementById('like-button').style.display = 'inline-block';
        document.getElementById('liked-button').style.display = 'none';
    } else {
        document.getElementById('like-button').style.display = 'none';
        document.getElementById('liked-button').style.display = 'inline-block';
    }
}
function getLikes(postId) {
    $.ajax({
        type: 'GET',
        url: '/likes/' + postId,
        success: function (data) {
            setLikes(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}
function saveLike(postId) {
    document.getElementById('like-button').style.display = 'none';
    $.ajax({
        type: 'POST',
        url: '/likes/' + postId,
        success: function () {
                let data = {
                    totalLikes: totalLikes + 1,
                    hasLikedBefore: false
                }
                setLikes(data);
        },
        error: function (error) {
                console.log(error);
        }
    });
}

function removeLike(postId) {
    $.ajax({
        type: 'DELETE',
        url: '/likes/' + postId,
        contentType: "application/json",
        success: function () {
            let data = {
                totalLikes: totalLikes - 1,
                hasLikedBefore: true
            }
            setLikes(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

window.addEventListener('load', function() {
    let postId = document.getElementById('post-id').value;
    getLikes(postId);
});