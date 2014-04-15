package com.cs.layer3.repository.business.defalt.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QClazz is a Querydsl query type for Clazz
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QClazz extends EntityPathBase<Clazz> {

    private static final long serialVersionUID = -715457508L;

    public static final QClazz clazz = new QClazz("clazz");

    public final QBusinessObject _super = new QBusinessObject(this);

    public final StringPath classname = createString("classname");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QClazz(String variable) {
        super(Clazz.class, forVariable(variable));
    }

    public QClazz(Path<? extends Clazz> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClazz(PathMetadata<?> metadata) {
        super(Clazz.class, metadata);
    }

}

