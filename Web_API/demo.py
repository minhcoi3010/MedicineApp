from flask import Flask, render_template, url_for, redirect

app = Flask(__name__)

# Điều hướng Navigation
@app.route('/dashboard')
def dashboard():
      return render_template('dashboard.html', content = "Tuan Anh")

@app.route('/products')
def products():
      
      return render_template('products.html', content = "Tuan Anh")


# Demo flask
@app.route('/admin')
def admin():
      return "<h1> Hello admin </h1>"

@app.route('/user/<name>')
def user(name):
      if name == 'admin':
            return redirect(url_for('admin'))
      return f"<h1> Hello {name} </h1>"

if __name__ == "__main__":
      app.run(debug=True)     
