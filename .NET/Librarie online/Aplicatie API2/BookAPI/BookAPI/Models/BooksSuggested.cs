using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BookAPI.Models
{
    public class BooksSuggested
    {
        [Key]
        public int Id { get; set; }

        [Column(TypeName = "nvarchar(100)")]
        public string Name { get; set; } = "";

        [Column(TypeName = "nvarchar(100)")]
        public string Author { get; set; } = "";

        [Column(TypeName = "nvarchar(500)")]
        public string Picture_Url { get; set; } = "";

        [Column(TypeName = "nvarchar(500)")]
        public string Pdf_Url { get; set; } = "";

        [Column(TypeName = "nvarchar(1000)")]
        public string Description { get; set; } = "";
    }
}
