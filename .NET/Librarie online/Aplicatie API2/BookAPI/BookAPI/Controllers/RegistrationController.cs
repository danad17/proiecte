using BookAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace BookAPI.Controllers
{
    [Route("api/[controller]")]
    
    [ApiController]
    public class RegistrationController : ControllerBase
    {
        private readonly BookDbContext _db;

        public RegistrationController(BookDbContext db)
        {
            _db = db;
        }
        [HttpGet("GetUser/{userId}")]
        public async Task<ActionResult<UserMaster>> GetUser(int userId)
        {
            var user = await _db.Users.FindAsync(userId);

            if (user == null)
            {
                return NotFound();
            }

            return user;
        }
        [HttpGet]
        [Route("GetUserEmail/{userId}")]
        public IActionResult GetUserEmail(int userId)
        {
            // Assuming you have a User entity with an Email property
            var user = _db.Users.FirstOrDefault(u => u.UserId == userId);
            if (user == null)
            {
                return NotFound("User not found");
            }

            return Ok(new { email = user.Email });
        }
    
     /*   [HttpGet("GetUserByEmail/{email}")]
        public async Task<ActionResult<UserMaster>> GetUserByEmail(string email)
        {
            var user = await _db.Users.FirstOrDefaultAsync(u => u.Email == email);

            if (user == null)
            {
                return NotFound(); // Return 404 if user is not found
            }

            return user;
        }

        [HttpGet("GetUser/{userId}")]
        public async Task<IActionResult> GetUser(int userId)
        {
            var user = await _db.Users.FindAsync(userId);
            if (user == null)
            {
                return NotFound("User not found");
            }
            return Ok(user);
        }*/

        [HttpGet("current")]
        [Authorize]
        public async Task<ActionResult<UserMaster>> GetCurrentUser()
        {
            var userIdClaim = User.Claims.FirstOrDefault(c => c.Type == ClaimTypes.NameIdentifier);

            if (userIdClaim == null)
            {
                return NotFound("User not found");
            }

            if (!int.TryParse(userIdClaim.Value, out int userId))
            {
                return BadRequest("Invalid user ID");
            }

            var user = await _db.Users.FindAsync(userId);

            if (user == null)
            {
                return NotFound("User not found");
            }

            return Ok(user);
        }

        [HttpPost]
        [Route("Register")]
        [AllowAnonymous]
        public UserMaster AddUser(UserMaster obj)
        {
            _db.Users.Add(obj);
            _db.SaveChanges();
            return obj;
        }
        [HttpPost]
        [Route("Login")]
        [AllowAnonymous]
        public IActionResult Login([FromBody] LoginModel obj)
        {
            if (obj == null)
            {
                return BadRequest("Invalid client request");
            }

            var isUserExist = _db.Users.SingleOrDefault(m => m.UserName == obj.UserName && m.Password == obj.Password);
            if (isUserExist != null)
            {
                obj.UserId = isUserExist.UserId;
                obj.Result = true;
                obj.Message = "Login Success";
                obj.Role = isUserExist.Role;
                return Ok(obj);
            }
            else
            {
                obj.Result = false;
                obj.Message = "Username or Password is Wrong";
                return Unauthorized(obj);
            }
        }

    }
}