class MetaData:
    name = ''
    fields = dict.fromkeys(['name', 'val'])
    funcs = dict.fromkeys(['name', 'attr'])
    
    def __init__(self, n):
        self.name = n

    def set_fields(self, *f):
        self.fields = f

    def set_funcs(self, *f):
        self.funcs = f

    def add_func(self, name, attr):
        self.funcs[name] = attr

    def add_field(self, name, val):
        self.fields[name] = val

    def update_func(self, name, attr):
        self.funcs[name] = attr

    def update_field(self, name, val):
        self.fields[name] = val