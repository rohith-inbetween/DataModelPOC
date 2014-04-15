package com.cs.layer3.repository.business.defalt.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QClazzMapped is a Querydsl query type for ClazzMapped
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QClazzMapped extends EntityPathBase<ClazzMapped> {

    private static final long serialVersionUID = -565370513L;

    public static final QClazzMapped clazzMapped = new QClazzMapped("clazzMapped");

    public final QBusinessObject _super = new QBusinessObject(this);

    public final SetPath<Attribute, QAttribute> attributes = this.<Attribute, QAttribute>createSet("attributes", Attribute.class, QAttribute.class, PathInits.DIRECT2);

    public final StringPath classname = createString("classname");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QClazzMapped(String variable) {
        super(ClazzMapped.class, forVariable(variable));
    }

    public QClazzMapped(Path<? extends ClazzMapped> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClazzMapped(PathMetadata<?> metadata) {
        super(ClazzMapped.class, metadata);
    }

}

