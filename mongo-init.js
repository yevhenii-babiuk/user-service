db.createCollection("users")

db.users.createIndex(
    {"email":1},
    { unique: true }
)

db.users.insertMany([
    {
        "_id":"669bfd9a789c5c1f997ebaee",
        "email":"yevhenii.babiuk2@gmail.com",
        "password":"$2a$10$7PIQLf6PajyWTEDfNEhYS.4kBIDV96rvmwKp8hQb6y7Ac/QcuRp2i",
        "name":"Yevhenii2",
        "userExternalProjects":[
                {
                    "_id": '669c0f0afec5b91a023fdf24',
                    name: 'Project1'
                }
        ],
        "userRoles":[
             "ROLE_USER"
        ],
        "_class":"com.sky.yb.user.service.model.User"
    },
    {
        "_id":"669bfe65789c5c1f997ebaf3",
        "email":"yevhenii.babiuk@gmail.com",
        "password":"$2a$10$uz4slrEOHc1Kn5kHIgI84ey5r8gRWnaajyzIq7saxPSXddwCnxroe",
        "name":"Yevhenii",
        "userRoles":[
            "ROLE_ADMIN"
        ],
        "_class":"com.sky.yb.user.service.model.User"
    }
])
