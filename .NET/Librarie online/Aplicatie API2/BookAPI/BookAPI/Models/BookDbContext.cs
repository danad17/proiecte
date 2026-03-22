using Microsoft.EntityFrameworkCore;

namespace BookAPI.Models
{
    public class BookDbContext : DbContext
    {
        public BookDbContext(DbContextOptions options) : base(options)
        {
        }
        public DbSet<Books> Books { get; set; }
        public DbSet<UserMaster> Users { get; set; }
        public DbSet<BooksSuggested> BooksSuggested { get; set;}
    }
}
