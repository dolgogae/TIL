from typing import Any, Optional, Deque
from . import log

proxies: Any = ...

def manage(module, **params): ...
def clear_managers(): ...

reset_rollback: Any = ...
reset_commit: Any = ...
reset_none: Any = ...

class Pool(log.Identified):
    logging_name: str = ...
    echo: Any
    def __init__(self, creator, recycle: int = ..., echo: Optional[Any] = ..., use_threadlocal: bool = ...,
                 logging_name: Optional[Any] = ..., reset_on_return: bool = ..., listeners: Optional[Any] = ...,
                 events: Optional[Any] = ..., dialect: Optional[Any] = ...,
                 _dispatch: Optional[Any] = ...) -> None: ...
    def add_listener(self, listener): ...
    def unique_connection(self): ...
    def recreate(self): ...
    def dispose(self): ...
    def connect(self): ...
    def status(self): ...

class SingletonThreadPool(Pool):
    size: int = ...
    def __init__(self, creator, pool_size: int = ..., **kw) -> None: ...
    def recreate(self): ...
    def dispose(self): ...
    def status(self): ...

class QueuePool(Pool):
    def __init__(self, creator, pool_size: int = ..., max_overflow: int = ...,
                 timeout: int = ..., **kw) -> None: ...
    def recreate(self): ...
    def dispose(self): ...
    def status(self): ...
    def size(self): ...
    def checkedin(self): ...
    def overflow(self): ...
    def checkedout(self): ...

class NullPool(Pool):
    def status(self): ...
    def recreate(self): ...
    def dispose(self): ...

class StaticPool(Pool):
    def connection(self): ...
    def status(self): ...
    def dispose(self): ...
    def recreate(self): ...

class AssertionPool(Pool):
    def __init__(self, *args, **kw) -> None: ...
    def status(self): ...
    def dispose(self): ...
    def recreate(self): ...
