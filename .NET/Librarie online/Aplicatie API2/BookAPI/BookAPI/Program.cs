using BookAPI.Models;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", policy =>
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader());
});

builder.Services.AddDbContext<BookDbContext>(options =>
    options.UseMySql(
        builder.Configuration.GetConnectionString("BooksConnectionString"),
        ServerVersion.AutoDetect(builder.Configuration.GetConnectionString("BooksConnectionString"))
    ));

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors("AllowAll");        // ← PRIMUL, înainte de orice
app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();
app.Run();