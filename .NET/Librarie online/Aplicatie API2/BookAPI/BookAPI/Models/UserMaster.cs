using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BookAPI.Models
{
    [Table("UserMaster")]
    public class UserMaster {

        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int UserId { get; set; }

        [Column(TypeName = "nvarchar(50)")]
        public string UserName { get; set; } = "";

        [Column(TypeName = "nvarchar(50)")]
        public string Password { get; set; } = "";

        [Column(TypeName = "nvarchar(50)")]
        public string Role { get; set; } = "";

        [Column(TypeName = "nvarchar(50)")]
        public string FirstName { get; set; } = "";

        [Column(TypeName = "nvarchar(50)")]
        public string LastName { get; set; } = "";
        [Required, Column(TypeName = "nvarchar(100)")]
        public string Email { get; set; } = "";

        [Required]
        public DateTime CreatedDate { get; set; } = DateTime.UtcNow;
    }

    public class LoginModel
    {

        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int UserId { get; set; }
        public string UserName { get; set; } = "";
        [Column(TypeName = "nvarchar(50)")]
        public string Password { get; set; } = "";
        [Column(TypeName = "nvarchar(50)")]
        public string Role { get; set; } = "";
        public bool Result { get; set; }
        public string Message { get; set; }="";
    }
}
